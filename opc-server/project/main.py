import logging
import asyncio
import os
import sys
sys.path.insert(0, "..")

from asyncua import ua, Server
from asyncua.common.methods import uamethod

@uamethod
def func(parent, value):
    return value * 2

async def main():
    _logger = logging.getLogger('asyncua')
    # setup our server
    server = Server()
    await server.init()

    # 環境変数から設定値を取得
    url = os.environ['ACCESS_URL']
    print(url)
    server.set_endpoint(url)

    server.set_security_policy([
                ua.SecurityPolicyType.NoSecurity,
                ua.SecurityPolicyType.Basic256Sha256_SignAndEncrypt,
                ua.SecurityPolicyType.Basic256Sha256_Sign])

    # setup our own namespace, not really necessary but should as spec
    uri = 'http://examples.freeopcua.github.io'
    idx = await server.register_namespace(uri)

    # populating our address space
    # server.nodes, contains links to very common nodes like objects and root
    myobj = await server.nodes.objects.add_object(idx, 'ScalarTypes')
    # Set MyVariable to be writable by clients
    dev1 = await myobj.add_variable(1, 'Device1', 0.0)
    await dev1.set_writable()
    dev2 = await myobj.add_variable(2, 'Device2', -10.0)
    await dev2.set_writable()
    dev3 = await myobj.add_variable(3, 'Device3', 100.0)
    await dev3.set_writable()

    _logger.info('Starting server!')
    async with server:
        while True:
            await asyncio.sleep(1)
            dev1_val = await dev1.get_value()
            _logger.info('Set value of %s to %.1f', dev1, dev1_val)
            dev2_val = await dev2.get_value()
            _logger.info('Set value of %s to %.1f', dev2, dev2_val)
            dev3_val = await dev3.get_value()
            _logger.info('Set value of %s to %.1f', dev3, dev3_val)


if __name__ == '__main__':

    logging.basicConfig(level=logging.DEBUG)

    asyncio.run(main(), debug=True)