package com.monitor.display.vo.dependency;

import java.util.List;

/**
 * Created by eson on 2018/1/25.
 */
public class DependencyVo {

	private List<NodeVo>	nodes;
	private List<LinkVo>	links;



	public List<NodeVo> getNodes() {
		return nodes;
	}



	public void setNodes(List<NodeVo> nodes) {
		this.nodes = nodes;
	}



	public List<LinkVo> getLinks() {
		return links;
	}



	public void setLinks(List<LinkVo> links) {
		this.links = links;
	}
}
