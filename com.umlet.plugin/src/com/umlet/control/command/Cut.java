package com.umlet.control.command;

import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.UmletClipBoard;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class Cut extends Command {

	private Vector<Entity> entities;

	public Cut() {

	}

	@Override
	public void execute(DiagramHandler handler) {

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.defaultGridSize, false);

		super.execute(handler);
		if (this.entities == null) {
			this.entities = new Vector<Entity>();
			this.entities.addAll(handler.getSelectedEntities());
		}

		if (this.entities.isEmpty()) return;

		UmletClipBoard.getInstance().copy(this.entities, handler);
		(new RemoveElement(this.entities)).execute(handler);

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (Entity e : this.entities)
			(new AddEntity(e, e.getX(), e.getY())).execute(handler);
		handler.getDrawPanel().repaint();
	}
}
