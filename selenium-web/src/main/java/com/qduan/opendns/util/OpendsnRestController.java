package com.qduan.opendns.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qduan.opendns.api.OpendnsTask;

@RestController
public class OpendsnRestController {

	@Autowired
	Environment env;

	@Value("${youtube.domains}")
	List<String> youtubeDomains;

	@Value("${opendns_username}")
	String username;

	@Value("${opendns_password}")
	String password;

	@Value("${opendns_setings_network_id}")
	String setttingsId;

	@RequestMapping("/")
	public ResponseEntity<String> welcome() {
		return ResponseEntity.ok("welcome");
	}

	@RequestMapping("/list") 
	public ResponseEntity<String> getAll() {
		OpendnsTask task = new OpendnsTask(username, password, setttingsId);
		task.login();
		task.gotoSetings();
		Set<String> rval = task.getBlockedDomains();
		task.done();
		return ResponseEntity.ok("blocked domains: " + rval + "\n");
	}

	@RequestMapping("/block/{name}")
	public ResponseEntity<String> block(@PathVariable("name") String domainName) {

		OpendnsTask task = new OpendnsTask(username, password, setttingsId);
		task.login();
		task.gotoSetings();
		String msg = null;

		/*- if domain name xxx doesn't contain '.', then it treated as a group name defined by
		 *  domain.xxx=domain1,domain2,domain3
		 */
		if (domainName.indexOf(".") == -1 || domainName.indexOf(",") != -1) {
			List<String> domainNames = this.getDomaingroup(domainName);
			if (domainNames == null) {
				msg = ("Invalid domain name or group name specified: " + domainName);
				return ResponseEntity.badRequest().body(msg);
			}
			task.blockDomains(domainNames);
			msg = "domains blocked: " + domainNames + "\n";
		} else {
			task.blockDomain(domainName);
			msg = "domain blocked: " + domainName + "\n";
		}

		task.done();
		return ResponseEntity.ok(msg);
	}

	@RequestMapping("/unblock/{name}")
	public ResponseEntity<String> unblock(@PathVariable("name") String domainName) {
		OpendnsTask task = new OpendnsTask(username, password, setttingsId);

		task.login();
		task.gotoSetings();
		String msg = null;
		/*- if domain name xxx doesn't contain '.', then it treated as a group name defined by
		 *  xxx.domains=domain1,domain2,domain3
		 */
		if (domainName.indexOf(".") == -1 || domainName.indexOf(",") != -1) {
			List<String> domainNames = this.getDomaingroup(domainName);
			if (domainNames == null) {
				msg = ("Invalid domain name or group name specified: " + domainName);
				return ResponseEntity.badRequest().body(msg);
			}
			task.unblockDomains(domainNames);
			msg = "domains blocked: " + domainNames + "\n";
		} else {
			task.unblockDomain(domainName);
			msg = "domain blocked: " + domainName + "\n";
		}
		task.done();
		return ResponseEntity.ok(msg);
	}

	@RequestMapping("/clear")
	public ResponseEntity<String> clearAll() {
		OpendnsTask task = new OpendnsTask(username, password, setttingsId);

		task.login();
		task.gotoSetings();

		task.deleteAllBlockedDomains();
		task.done();
		return ResponseEntity.ok("all domains cleared.\n");
	}

	/**
	 * @param domainGroup
	 * @return
	 */
	protected List<String> getDomaingroup(String domainGroup) {
        String domainNames = domainGroup; //multiple actual domain names
        //youtube
		if (domainGroup.indexOf(".") == -1) {
			domainNames = env.getProperty(domainGroup + ".domains");
			if (domainNames == null || domainNames.length() == 0) {
				return null;
			}
		}

		return Arrays.asList(domainNames.split(","));
	}
}
