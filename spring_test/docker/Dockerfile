FROM openjdk:8-jre-alpine

USER root

COPY ./localedata /localedata

RUN apk add tzdata

RUN apk add curl

RUN apk --no-cache add ca-certificates

RUN cd /localedata && apk add glibc-bin-2.25-r0.apk glibc-i18n-2.25-r0.apk glibc-2.25-r0.apk \

RUN cat /localedata/locale.md | xargs -i /usr/glibc-compat/bin/localedef -i {} -f UTF-8 {}.UTF-8 \

RUN  /usr/glibc-compat/bin/localedef -c -f UTF-8 -i zh_CN zh_TW.utf8

RUN apk del ca-certificates

ENV LANGUAGE=zh_TW.UTF-8

ENV LC_ALL=zh_TW.UTF-8

ENV LANG=zh_TW.UTF-8

ENV TZ=Asia/Taipei

ADD jar/org /proj/src/app/org

ADD jar/BOOT-INF/lib /proj/src/app/BOOT-INF/lib

ADD jar/META-INF /proj/src/app/META-INF

ADD jar/BOOT-INF/classes /proj/src/app/BOOT-INF/classes

WORKDIR /proj/src/app

EXPOSE 8080/tcp

# java -cp . org.springframework.boot.loader.JarLauncher
ENTRYPOINT [ "java" , "-cp" , "." , "org.springframework.boot.loader.JarLauncher" ]

