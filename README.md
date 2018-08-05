# kotlin-multiaddr

[![](https://img.shields.io/badge/made%20by-Protocol%20Labs-blue.svg?style=flat-square)](http://ipn.io)
[![](https://img.shields.io/badge/project-multiformats-blue.svg?style=flat-square)](https://github.com/multiformats/multiformats)
[![](https://img.shields.io/badge/freenode-%23ipfs-blue.svg?style=flat-square)](https://webchat.freenode.net/?channels=%23ipfs)
[![](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

> [multiaddr](https://github.com/multiformats/multiaddr) implementation in kotlin

Multiaddr is a standard way to represent addresses that:

- Support any standard network protocols.
- Self-describe (include protocols).
- Have a binary packed format.
- Have a nice string representation.
- Encapsulate well.

## Install


#### Maven

```maven
<dependency>
  <groupId>io.ipfs.multiformats</groupId>
  <artifactId>kotlin-multiaddr</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

#### Gradle

```gradle
compile 'io.ipfs.multiformats:kotlin-multiaddr:1.0.0'
```


## Usage


```kotlin
//encapsulate
val md1 = Multiaddr("/ip4/127.0.0.1/udp/1234")
val md2 = Multiaddr("/udp/5678")
val str = md1.encapsulate(md2).toString()

//decapsulate
val md1 = Multiaddr("/ip4/127.0.0.1/udp/5678/ip4/127.0.0.1/udp/1234/udp/1234")
val md2 = Multiaddr("/ip4/127.0.0.1")
val str = md1.decapsulate(md2).toString()
```


## Maintainers

Captain: [@changjiashuai](https://github.com/changjiashuai).

## Contribute

Contributions welcome. Please check out [the issues](https://github.com/changjiashuai/kotlin-multiaddr/issues).

Check out our [contributing document](https://github.com/multiformats/multiformats/blob/master/contributing.md) for more information on how we work, and about contributing in general. Please be aware that all interactions related to multiformats are subject to the IPFS [Code of Conduct](https://github.com/ipfs/community/blob/master/code-of-conduct.md).

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

[MIT](LICENSE) © 2016 Protocol Labs Inc.
