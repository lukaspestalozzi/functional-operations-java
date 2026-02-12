# Maven Proxy Workaround for Restricted Environments

## Problem

In Claude Code remote containers, Maven cannot resolve DNS for `repo.maven.apache.org` because the JVM's DNS resolver does not use the system HTTP proxy. This causes all `mvn` commands to fail with:

```
Could not transfer artifact ... from/to central: transfer failed for https://repo.maven.apache.org/maven2/...
```

or:

```
No plugin found for prefix 'spotless' ...
```

Meanwhile, `curl` works fine because it honors `HTTP_PROXY` / `HTTPS_PROXY` environment variables.

## Solution

Create `~/.m2/settings.xml` with proxy settings extracted from the environment's `HTTP_PROXY` variable. Run this **before** any Maven commands:

```bash
python3 -c "
from urllib.parse import urlparse
import os

url = urlparse(os.environ['HTTP_PROXY'])
settings = f'''<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<settings xmlns=\"http://maven.apache.org/SETTINGS/1.2.0\"
  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
  xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd\">
  <proxies>
    <proxy>
      <id>http-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>{url.hostname}</host>
      <port>{url.port}</port>
      <username>{url.username}</username>
      <password>{url.password}</password>
    </proxy>
    <proxy>
      <id>https-proxy</id>
      <active>true</active>
      <protocol>https</protocol>
      <host>{url.hostname}</host>
      <port>{url.port}</port>
      <username>{url.username}</username>
      <password>{url.password}</password>
    </proxy>
  </proxies>
</settings>
'''
with open(os.path.expanduser('~/.m2/settings.xml'), 'w') as f:
    f.write(settings)
print('Created ~/.m2/settings.xml with proxy settings')
"
```

Then clear any cached download failures:

```bash
find ~/.m2/repository -name "*.lastUpdated" -delete
```

## Running Maven

Always use the `-Dmaven.resolver.transport=wagon` flag (as noted in CLAUDE.md):

```bash
mvn clean verify -Dmaven.resolver.transport=wagon
```

For Spotless formatting, use the fully qualified plugin name:

```bash
mvn com.diffplug.spotless:spotless-maven-plugin:apply -Dmaven.resolver.transport=wagon
```

## Quick One-Liner Check

To verify Maven Central is reachable before running builds:

```bash
curl -s --connect-timeout 5 -o /dev/null -w "%{http_code}" https://repo.maven.apache.org/maven2/
```

A `200` response means the proxy is working and Maven should succeed after configuring `settings.xml`.

## Notes

- The `./mvnw` wrapper will NOT work — it tries to download the Maven distribution itself and fails on DNS before it can use settings.xml.
- Use the system-installed `mvn` command instead.
- The proxy JWT token expires periodically. If builds start failing after previously working, regenerate `settings.xml` by re-running the Python script above.
