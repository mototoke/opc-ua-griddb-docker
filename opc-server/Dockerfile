FROM python:3.8

WORKDIR /root

RUN python3.8 -m venv .venv
RUN . .venv/bin/activate && \
    python -V && \
    python -m pip install asyncua
RUN echo '. /root/.venv/bin/activate' >> .bashrc

WORKDIR /root/project

# CMD uaserver
CMD . /root/.venv/bin/activate && python main.py