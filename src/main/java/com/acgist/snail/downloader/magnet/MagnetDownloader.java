package com.acgist.snail.downloader.magnet;

import java.io.IOException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.snail.downloader.Downloader;
import com.acgist.snail.pojo.bean.Magnet;
import com.acgist.snail.pojo.session.TaskSession;
import com.acgist.snail.pojo.session.TorrentSession;
import com.acgist.snail.protocol.magnet.bootstrap.MagnetReader;
import com.acgist.snail.system.exception.DownloadException;
import com.acgist.snail.system.manager.TorrentManager;
import com.acgist.snail.utils.ThreadUtils;

public class MagnetDownloader extends Downloader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MagnetDownloader.class);

	private TorrentSession torrentSession;
	
	private Object downloadLock = new Object(); // 下载锁
	
	public MagnetDownloader(TaskSession taskSession) {
		super(taskSession);
		loadTorrent();
	}
	
	public static final MagnetDownloader newInstance(TaskSession taskSession) {
		return new MagnetDownloader(taskSession);
	}

	@Override
	public void open() {
	}

	@Override
	public void download() throws IOException {
		while(ok()) {
			synchronized (downloadLock) {
				ThreadUtils.wait(downloadLock, Duration.ofSeconds(Integer.MAX_VALUE));
				this.complete = torrentSession.torrentStreamGroup().complete();
			}
		}
		// TODO：保存种子文件路径-转换磁力链接
	}

	@Override
	public void release() {
	}
	
	
	/**
	 * <p>加载任务</p>
	 * <p>创建时立即加载任务，使任务可以被分享。</p>
	 */
	private void loadTorrent() {
		final var entity = this.taskSession.entity();
		final String path = entity.getTorrent();
		try {
			final Magnet magnet = MagnetReader.newInstance(entity.getUrl()).magnet();
			final String infoHashHex = magnet.getHash();
			this.torrentSession = TorrentManager.getInstance().newTorrentSession(infoHashHex, path);
		} catch (DownloadException e) {
			fail("任务加载失败");
			LOGGER.error("任务加载异常", e);
			return;
		}
	}

}
