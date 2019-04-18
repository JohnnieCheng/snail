package com.acgist.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.acgist.snail.net.peer.PeerClient;
import com.acgist.snail.pojo.entity.TaskEntity;
import com.acgist.snail.pojo.entity.TaskEntity.Type;
import com.acgist.snail.pojo.session.PeerSession;
import com.acgist.snail.pojo.session.StatisticsSession;
import com.acgist.snail.pojo.session.TaskSession;
import com.acgist.snail.pojo.session.TorrentSession;
import com.acgist.snail.system.exception.DownloadException;
import com.acgist.snail.system.manager.TorrentSessionManager;
import com.acgist.snail.utils.JsonUtils;

public class PeerClientTest {
	
	@Test
	public void test() throws DownloadException, InterruptedException {
		String path = "e:/snail/123.torrent";
//		String path = "e:/snail/1234.torrent";
//		String path = "e:/snail/12345.torrent";
//		String path = "e:/snail/123456.torrent";
		TorrentSession torrentSession = TorrentSessionManager.getInstance().buildSession(path);
		var files = torrentSession.torrent().getInfo().files();
		List<String> list = new ArrayList<>();
		AtomicLong size = new AtomicLong(0);
		files.forEach(file -> {
			if(!file.path().contains("_____padding_file")) {
				list.add(file.path());
				if(file.path().contains("Scans")) {
				}
			}
			size.addAndGet(file.getLength());
		});
		TaskEntity entity = new TaskEntity();
		entity.setFile("e://tmp/test/");
		entity.setType(Type.torrent);
		entity.setDescription(JsonUtils.toJson(list));
		torrentSession.build(TaskSession.newInstance(entity));
//		String host = "185.45.195.167";
//		Integer port = 20009;
		String host = "192.168.1.100";
		Integer port = 49160; // FDM测试端口
//		Integer port = 15000; // 本地迅雷测试端口
		System.out.println("已下载：" + torrentSession.torrentStreamGroup().pieces());
		PeerSession peerSession = new PeerSession(new StatisticsSession(), host, port);
		PeerClient client = new PeerClient(peerSession, torrentSession);
		client.download();
		Thread.sleep(Long.MAX_VALUE);
	}

}
