package de.cluetec.showcase.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.cluetec.showcase.ApplicationConfiguration;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@ActiveProfiles({"test"})
public abstract class AbstractTodoAppTest {

	private static final String DATABASE = "hska-showcase-tests";
	private static String HOST = "localhost";
	private static int PORT = 27072;

	private static final MongodStarter MONGODB_STARTER = MongodStarter.getDefaultInstance();
	private static MongodExecutable MONGOD_EXE;
	private static MongodProcess MONGOD;

	private DB database;

	@BeforeClass
	public static void startup() throws UnknownHostException, IOException {
		IMongodConfig config = new MongodConfigBuilder()//
				.version(Version.Main.PRODUCTION)//
				.net(new Net(PORT, Network.localhostIsIPv6()))//
				.build();

		HOST = config.net().getServerAddress().getHostAddress();
		PORT = config.net().getPort();

		MONGOD_EXE = MONGODB_STARTER.prepare(config);
		MONGOD = MONGOD_EXE.start();
	}

	@AfterClass
	public static void shutdown() {
		MONGOD.stop();
		MONGOD_EXE.stop();
	}

	@Before
	public void setupMongoDB() throws UnknownHostException {
		MongoClient client = new MongoClient(HOST, PORT);
		database = client.getDB(DATABASE);
		database.dropDatabase();
	}
}
