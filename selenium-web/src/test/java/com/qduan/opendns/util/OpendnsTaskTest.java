package com.qduan.opendns.util;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qduan.opendns.api.OpendnsTask;

public class OpendnsTaskTest {


	OpendnsTask task = new OpendnsTask("duanqiang@gmail.com", "XX", "settingsId");

	@Test
	public void testLogin() {
		task.login();
	}

	@Test
	public void unblockAll() {
		task.login();
		task.gotoSetings();
		task.deleteAllBlockedDomains();
		task.done();
	}

	@Test
	public void blockDomain() {
		String domainName = "www.yahoo.com";
		task.blockDomain(domainName);
		Set<String> blockedDomains = task.getBlockedDomains();

	}
}
