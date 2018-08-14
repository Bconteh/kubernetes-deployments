FROM docker:17.10.0-ce-git

RUN apk update && apk add \
    curl \
    net-tools \
    iptables


# RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.11.0/bin/linux/amd64/kubectl
# RUN wget https://storage.googleapis.com/kubernetes-release/release/v1.11.2/bin/linux/amd64/kubectl -O kubectl

RUN apk --no-cache add gettext ca-certificates openssl \
    && wget https://storage.googleapis.com/kubernetes-release/release/v1.11.2/bin/linux/amd64/kubectl -O kubectl \
    && chmod a+x kubectl \
    && apk --no-cache del ca-certificates openssl

#RUN chmod +x ./kubectl
RUN  mv ./kubectl /usr/local/bin/kubectl

