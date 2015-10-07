package src.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Observable;

import src.model.Model;
import cs355.GUIFunctions;
import cs355.model.drawing.*;
import cs355.view.ViewRefresher;

public class View implements ViewRefresher
{
	public Rectangle2D rect = new Rectangle2D.Double(10, 10, 10, 10);

	@Override
	public void update(Observable arg0, Object arg1) 
	{
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) 
	{
		// TODO Auto-generated method stub
		ArrayList<Shape> currentShapes = (ArrayList<Shape>) Model.get().getShapes();
		
		for(int i = 0; i < currentShapes.size(); i++)
		{
			Shape focus = currentShapes.get(i);
			
			//draw primary shape
			g2d.setColor(focus.getColor());
			if(shapeFactory(focus) != null)
			{
				g2d.fill(shapeFactory(focus));
				g2d.draw(shapeFactory(focus));
			}
			
			//draw handles if the shape is active
			if((shapeFactory(focus) != null) && (focus.getActive()))
			{
				g2d.setColor(Color.WHITE);
				g2d.draw(shapeFactory(focus));
			}
		}
	}
	
	public java.awt.Shape shapeFactory(Shape focus)
	{
		if(focus instanceof Line)
		{
			Line focusLine = (Line)focus;
			Point2D.Double start = new Point2D.Double(focusLine.getCenter().x, focusLine.getCenter().y);		
			Point2D.Double end = new Point2D.Double(focusLine.getEnd().x, focusLine.getEnd().y);
			
			return new Line2D.Double(start.x, start.y, end.x, end.y);
		}
		
		if(focus instanceof Square)
		{
			Square active = (Square) focus;
			return new Rectangle2D.Double(active.getUpperLeft().x, active.getUpperLeft().y, active.getSize(), active.getSize());
		}
		
		if(focus instanceof Rectangle)
		{
			Rectangle active = (Rectangle) focus;
			return new Rectangle2D.Double(active.getUpperLeft().x, active.getUpperLeft().y, active.getWidth(), active.getHeight());
		}
		
		if(focus instanceof Circle)
		{
			Circle active = (Circle) focus;
			return new Ellipse2D.Double(active.getUpperLeft().x, active.getUpperLeft().y, active.getRadius() * 2, active.getRadius() * 2);
		}
		
		if(focus instanceof Ellipse)
		{
			Ellipse active = (Ellipse) focus;
			return new Ellipse2D.Double(active.getUpperLeft().x, active.getUpperLeft().y, active.getWidth(), active.getHeight());
		}
		
		if(focus instanceof Triangle)
		{
			if(focus.getCenter() == null)
				return null;
			
			Triangle active = (Triangle) focus;
			int[] x = new int[3];
			int[] y = new int[3];
			
			x[0] = (int)active.getCenter().x - (int) active.getA().x;
			x[1] = (int)active.getCenter().x - (int) active.getB().x;
			x[2] = (int)active.getCenter().x - (int) active.getC().x;
			
			y[0] = (int)active.getCenter().y - (int) active.getA().y;
			y[1] = (int)active.getCenter().y - (int) active.getB().y;
			y[2] = (int)active.getCenter().y - (int) active.getC().y;
			
			Polygon tri = new Polygon();
			tri.addPoint(x[0], y[0]);
			tri.addPoint(x[1], y[1]);
			tri.addPoint(x[2], y[2]);
			return tri;
		}
		
		return null;
	}

}
