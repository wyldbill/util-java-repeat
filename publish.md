# Publishing to MAven Central
There's a bunch of stuff to do here:

[First, get set up](https://central.sonatype.org/publish/publish-guide/#introduction)

Part of publishing is signing files. [there's a good write up here](https://central.sonatype.org/publish/requirements/gpg/)
Don't forget to safegaurd your privae keyring. I left mine behind when I changed 
machines and had to re-create it.

In order to use your signing key to publish from a Github Action, it'll
need to make into a github secret. [Here's a great gist](https://gist.github.com/sualeh/ae78dc16123899d7942bc38baba5203c)
I learned to export my key so I can store it in my cloud based password manager
as well.

https://github.com/gradle-nexus/publish-plugin/
https://blog.solidsoft.pl/2015/09/08/deploy-to-maven-central-using-api-key-aka-auth-token/
