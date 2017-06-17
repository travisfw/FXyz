/*
 * Copyright (C) 2013-2015 F(X)yz, 
 * Sean Phillips, Jason Pollastrini and Jose Pereda
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

import org.fxyz.shapes.containers.ShapeContainer;
import org.fxyz.shapes.primitives.OctahedronMesh;
import org.fxyz.shapes.primitives.RhombicDodecahedronMesh;


public class RhombicDodecahedron extends ShapeContainer<RhombicDodecahedronMesh> {

	private RhombicDodecahedronMesh mesh;
	
	public RhombicDodecahedron() {
		super(new RhombicDodecahedronMesh());
		this.mesh = getShape();
	}
	
	public RhombicDodecahedron(double height) {
		this();
		mesh.setHeight(height);
	}
	
	public RhombicDodecahedron(Color c){
        this();
        this.setDiffuseColor(c);
    }
    
	public RhombicDodecahedron(double height, Color c){
        super(new RhombicDodecahedronMesh(height));
        this.mesh = getShape();
        this.setDiffuseColor(c);
    }
	
    public final void setHeight(double value) {
        mesh.setHeight(value);
    }
    public final void setMaterial(Material value) {
        mesh.setMaterial(value);
    }
    public final void setDrawMode(DrawMode value) {
        mesh.setDrawMode(value);
    }
    public final void setCullFace(CullFace value) {
        mesh.setCullFace(value);
    }
    public final double getHeight() {
        return mesh.getHeight();
    }
    public DoubleProperty heightProperty() {
        return mesh.heightProperty();
    }
	
}
