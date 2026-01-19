FROM amazoncorretto:21-al2023

EXPOSE 19132/tcp
EXPOSE 19132/udp

WORKDIR /home

ADD build/libs/bamboo-proxy.jar /home/bamboo-proxy.jar

ENTRYPOINT ["java", \
    "-Dfile.encoding=UTF-8", \
    "-Dterminal.ansi=true", \
    "-XX:+UseZGC", \
    "-XX:+ZGenerational", \
    "-XX:+UseStringDeduplication", \
    "-Xmx3G", \
    "-Xms1G", \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
    "--add-opens", "java.base/java.io=ALL-UNNAMED", \
    "-jar", "bamboo-proxy.jar" \
]