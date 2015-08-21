package test.test.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * Hello world!
 *
 */
public class App {

	static class ZWatcher implements Watcher {
		private ZooKeeper zk;
		private String path;

		ZWatcher() {
			this(null, "root");
		}

		ZWatcher(ZooKeeper zk, String path) {
			this.zk = zk;
			this.path = path;
		}

		public void process(WatchedEvent event) {
			System.out.println(path + ":" + event.getState() + " " + event.getType() + " " + event.getPath());

			if (zk != null && path != null) {
				if (event.getState() == KeeperState.SyncConnected) {
					try {
						zk.exists(path, this);
					} catch (Exception e) {
						e.printStackTrace(System.out);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

		final ZooKeeper zk = new ZooKeeper("localhost:2000", 3000, new ZWatcher());

		// zk.register(new ZWatcher(zk, null));

		System.out.println("list all nodes under root:");
		List<String> pathes = listNodes(zk, "/");
		for (String path : pathes) {
			System.out.println(path);
		}

		if (zk.exists("/test", false) != null) {
			System.out.println("remove all nodes /test");
			ZKUtil.deleteRecursive(zk, "/test");
		}

		zk.exists("/", new ZWatcher(zk, "/"));
		zk.exists("/test", new ZWatcher(zk, "/test"));
		zk.exists("/test/abc", new ZWatcher(zk, "/test/abc"));

		zk.create("/test", new byte[] { 1 }, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/test/abc", new byte[] { 1 }, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.setData("/test/abc", new byte[] { 2 }, -1);
		zk.delete("/test/abc", -1);
		zk.delete("/test", -1);

		System.out.println("Press any key to close and exit");
		System.in.read();
		zk.close();

	}

	private static List<String> listNodes(ZooKeeper zk, String pathRoot) throws KeeperException, InterruptedException {

		List<String> tree = new ArrayList<String>();
		if (zk.exists(pathRoot, false) == null) {
			return tree;
		}
		tree.add(pathRoot);
		Deque<String> queue = new LinkedList<String>();
		queue.add(pathRoot);

		while (true) {
			String node = queue.pollFirst();
			if (node == null) {
				break;
			}
			List<String> children = zk.getChildren(node, false);
			for (final String child : children) {
				final String childPath = node + (node.charAt(node.length() - 1) == '/' ? child : '/' + child);
				queue.add(childPath);
				tree.add(childPath);
			}
		}
		return tree;
	}

}
