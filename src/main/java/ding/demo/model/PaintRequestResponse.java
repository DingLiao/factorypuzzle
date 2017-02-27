package ding.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaintRequestResponse {
	class Link {
		private String name;
		private String uri;
		public Link(String name, String uri) {
			super();
			this.name = name;
			this.uri = uri;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
	}
	
	private String name;
	private Date created;
	private List<Link> links = new ArrayList<Link>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public void addLink(String name, String uri) {
		this.links.add(new Link(name, uri));
	}
	
}
