package com.example.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperty {

	private Prop master;

	private List<Prop> slave;

	static class Prop {

		private String url;

		private String username;

		private String password;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "Prop{" + "url='" + url + '\'' + ", username='" + username + '\'' + ", password='" + password + '\''
					+ '}';
		}
	}

	public Prop getMaster() {
		return master;
	}

	public void setMaster(Prop master) {
		this.master = master;
	}

	public List<Prop> getSlave() {
		return slave;
	}

	public void setSlave(List<Prop> slave) {
		this.slave = slave;
	}

	@Override
	public String toString() {
		return "DataSourceProperty{" + "master=" + master + ", slave=" + slave + '}';
	}
}
