/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, a	nd is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.draw2d.graph.VirtualNode;

/**
 * Encapsulates the conversion of a long edge to multiple short edges and back.
 * @since 3.1
 */
public class VirtualNodeCreation extends RevertableChange {

private final Edge edge;
private final DirectedGraph graph;
private Node nodes[];
private Edge[] edges;

/**
 * Breaks a single edge into multiple edges containing virtual nodes.
 * @since 3.1
 * @param edge The edge to convert
 * @param graph the graph containing the edge
 */
public VirtualNodeCreation(Edge edge, DirectedGraph graph) {
	this.edge = edge;
	this.graph = graph;

	int size = edge.target.rank - edge.source.rank - 1;
	int offset = edge.source.rank + 1;

	Node prevNode = edge.source;
	Node currentNode;
	Edge currentEdge;
	nodes = new Node[size];
	edges = new Edge[size + 1];
	
	Insets padding = new Insets(0, edge.padding, 0, edge.padding);
	
	Subgraph s = GraphUtilities.getCommonAncestor(edge.source, edge.target);
	
	for (int i = 0; i < size; i++) {
		nodes[i] = currentNode = new VirtualNode("Virtual" + i + ':' + edge, s); //$NON-NLS-1$
		currentNode.width = edge.width;
		if (s != null) {
			currentNode.nestingIndex = s.nestingIndex;
		}

		currentNode.height = 0;
		currentNode.setPadding(padding);
		currentNode.rank = offset + i;
		graph.ranks.getRank(offset + i).add(currentNode);
		
		currentEdge = new Edge(prevNode, currentNode, 1, edge.weight * 8);
		if (i == 0) {
			currentEdge.weight = edge.weight * 2;
			currentEdge.offsetSource = edge.offsetSource;
		}
		graph.edges.add(edges[i] = currentEdge);
		graph.nodes.add(currentNode);
		prevNode = currentNode;
	}
	
	currentEdge = new Edge(prevNode, edge.target, 1, edge.weight * 2);
	currentEdge.offsetTarget = edge.offsetTarget;
	graph.edges.add(edges[edges.length - 1] = currentEdge);
	graph.removeEdge(edge);
}

/**
 * @see org.eclipse.draw2d.internal.graph.RevertableChange#revert()
 */
public void revert() {
	edge.start = edges[0].start;
	edge.end = edges[edges.length - 1].end;
	edge.vNodes = new NodeList();
	for (int i = 0; i < edges.length; i++) {
		graph.removeEdge(edges[i]);
	}
	for (int i = 0; i < nodes.length; i++) {
		edge.vNodes.add(nodes[i]);
		graph.removeNode(nodes[i]);
	}
	edge.source.outgoing.add(edge);
	edge.target.incoming.add(edge);
	
	graph.edges.add(edge);
}

}