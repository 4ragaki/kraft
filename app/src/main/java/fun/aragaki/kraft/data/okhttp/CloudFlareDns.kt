package `fun`.aragaki.kraft.data.okhttp

import okhttp3.Dns
import java.net.InetAddress

class CloudFlareDns(private val service: CloudFlareDNSService) : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val addresses = mutableListOf<InetAddress>()

        val response = service.query(name = hostname).execute().body()
        response?.Answer?.flatMap {
            InetAddress.getAllByName(it.data).toList()
        }?.run {
            addresses.addAll(this)
        }

        return addresses
    }
}