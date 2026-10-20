# Security and privacy

The current launcher has no Internet permission and no account, analytics backend, or remote update client. Preferences remain in local app storage; Android app backup is disabled. Apps opened by Open TV follow their own privacy policies.

Network/Wi-Fi state permissions support the connection indicator. Fine location is declared for optional Wi-Fi-name support on compatible firmware but is not requested at runtime or granted by the build. Without it, connection type is shown instead.

The Home button accessibility service is optional. When enabled, it filters HOME and ALL_APPS key events, passes other keys through, does not store key events, and cannot retrieve window content. The separate inherited Input-button service is not part of the documented public setup.

## Reporting a vulnerability

Use **Security → Report a vulnerability** on the canonical `PatilSarvesh/Open-TV` GitHub repository when private reporting is enabled. Do not post exploit details, credentials, or personal logs in a public issue. If that private channel is unavailable, open an issue requesting a private contact without disclosing the vulnerability; maintainers must establish a private channel before requesting details.

Only the current development version is maintained at this stage. Public release signing and an alpha distribution are still pending; see [docs/RELEASING.md](docs/RELEASING.md).
