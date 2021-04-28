# Android_Projects
There are a lot of projects here in this Repo but my favourite is the End-to-End Encryption Android Application. 
This project was part of my final year project @ Trinity College Dublin. The project proposed a next-generation network that uses a Blockchain-=based Public
Key Infrastructure mechanism and proposes a new E2EE mechanism. I also did an in-depth analysis of WhatsApp's E2EE architecture and used that to propose a new
E2EE framework. Users casn also read my paper on this project @AXriv: https://arxiv.org/abs/2104.08494 ; Enjoy! 
It took me 2 months to build this app, and hopefully it will get me a good grade, maybe, just maybe; lol/

The App has the following features:(All features ared to end encrypted)
* One on One Chat Session
* Group Chat
* Local SQLite Database
* Certificate Signing Request(CSR) generation
* Login and Register Mechanism

The App uses the following technologies:
* Firebase as a Chat Server
* SQLite
* Bouncy Castle for CSR generation
* ECDH-Curve25519-Mobile for ECDH shared key derivation
* AES256 in CBC mode for Symmetric Encryption
* HMAC-SHA256 for Message Authentication
