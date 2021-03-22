# Programming Challenge

Dear XYZ Team,

Thanks for giving me an opportunity to solve the challenge, I spent some lovely time working on it.

I didn't even start with Frontend part of the challenge since I have a little experience with Angular.
Also, I spent quite some time on Backend part and decided that working on Frontend would be out of time limit.

## Backend module

### Challenge 1: API Design

You can find result inside ```antti-marsgate-1.0.0-resolved.yaml```.
I tried to keep API as simple as possible and didn't touch too much versioning and security topics here.

### Challenge 2: Completing and extending the java application

Fixed and extended application you can find in this workspace.
I had to redesign services a bit and mostly simplify FileService.
Also extend task story to be able to add new types of tasks and implement scheduler for cleaning.
You can find updated version of API here ```src/main/resources/swagger.yaml```

Most interested part is about counter task execution. I saw that you've mentioned

*- Multithreading / Locking execution*

But please bear with my trick :-) (what you apparently didn't do) I decided that if it is possible to keep solution simple then why not.
Yes we could discuss implementation which includes concurrency usage if you'd like.

From the original spec it is possible (in my opinion) not to spawn additional threads for counter tasks, see implementation.

And one note

*If you decide to copy code, please mark it as copied citing the source.*

Of course all that code not completely came out of my head. Even when I didn't google I could use some older snippets 
from my pet projects (which means I could google before :-)). But I didn't copy any solid part of the code from somewhere, 
may be just some ideas, that's why I don't include any sources. 

### How to build & test the application

To build it just type from project directory 

```mvn clean package spring-boot:repackage```

after that you can launch the application

```cd target && java -jar task-service.jar```

You can also test it.
For unit tests please do this.

```mvn clean verify```

For integration tests when the real application started and being used in different scenarios

```mvn clean verify -Pintegration```

Integration test could be also launched from IDE, see ```com.sinolec.challenge.controllers.TaskControllerIT```

Enjoy!

SY, Antti
