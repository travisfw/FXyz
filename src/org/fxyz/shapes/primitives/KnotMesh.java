package org.fxyz.shapes.primitives;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.DepthTest;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.TriangleMesh;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.primitives.helper.KnotHelper;
import org.fxyz.utils.GaussianQuadrature;

/**
 *  Spring based on this model:  http://en.wikipedia.org/wiki/Trefoil_knot
 *  Wrapped around a torus: http://mathoverflow.net/a/91459
    *  Using Frenet-Serret trihedron: http://mathematica.stackexchange.com/a/18612
 */
public class KnotMesh extends TexturedMesh {

    private static final double DEFAULT_MAJOR_RADIUS = 2.0D;
    private static final double DEFAULT_MINOR_RADIUS = 1.0D;
    private static final double DEFAULT_WIRE_RADIUS = 0.2D;
    private static final double DEFAULT_P = 2.0D; 
    private static final double DEFAULT_Q = 3.0D; 
    private static final double DEFAULT_LENGTH = 2.0D*Math.PI*DEFAULT_Q; 
    
    private static final int DEFAULT_LENGTH_DIVISIONS = 200;
    private static final int DEFAULT_WIRE_DIVISIONS = 50;
    private static final int DEFAULT_LENGTH_CROP = 0;
    private static final int DEFAULT_WIRE_CROP = 0;
    
    private static final double DEFAULT_START_ANGLE = 0.0D;
    private static final double DEFAULT_X_OFFSET = 0.0D;
    private static final double DEFAULT_Y_OFFSET = 0.0D;
    private static final double DEFAULT_Z_OFFSET = 1.0D;
    
    private KnotHelper knot;
    
    public KnotMesh() {
        this(DEFAULT_MAJOR_RADIUS, DEFAULT_MINOR_RADIUS, DEFAULT_WIRE_RADIUS, DEFAULT_P, DEFAULT_Q,
             DEFAULT_LENGTH_DIVISIONS, DEFAULT_WIRE_DIVISIONS, DEFAULT_LENGTH_CROP, DEFAULT_WIRE_CROP);
    }

    public KnotMesh(double majorRadius, double minorRadius, double wireRadius, double p, double q) {
        this(majorRadius, minorRadius, wireRadius, p, q, 
             DEFAULT_LENGTH_DIVISIONS, DEFAULT_WIRE_DIVISIONS, DEFAULT_LENGTH_CROP, DEFAULT_WIRE_CROP);
    }

    public KnotMesh(double majorRadius, double minorRadius, double wireRadius, double p, double q, 
                      int rDivs, int tDivs, int lengthCrop, int wireCrop) {
        
        setMajorRadius(majorRadius);
        setMinorRadius(minorRadius);
        setWireRadius(wireRadius);
        setP(p);
        setQ(q);
        setLengthDivisions(rDivs);
        setWireDivisions(tDivs);
        setLengthCrop(lengthCrop);
        setWireCrop(wireCrop);
        
        updateMesh();
        setCullFace(CullFace.BACK);
        setDrawMode(DrawMode.FILL);
        setDepthTest(DepthTest.ENABLE);
    }

    @Override
    protected final void updateMesh(){   
        setMesh(null);
        mesh=createSpring((float) getMajorRadius(), (float) getMinorRadius(), (float) getWireRadius(), (float) getP(), (float) getQ(), (float) getLength(),
            getLengthDivisions(), getWireDivisions(), getLengthCrop(), getWireCrop(),
            (float) getTubeStartAngleOffset(), (float)getxOffset(),(float)getyOffset(), (float)getzOffset());
        setMesh(mesh);
    }
    
    private final DoubleProperty majorRadius = new SimpleDoubleProperty(DEFAULT_MAJOR_RADIUS){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getMajorRadius() {
        return majorRadius.get();
    }

    public final void setMajorRadius(double value) {
        majorRadius.set(value);
    }

    public DoubleProperty majorRadiusProperty() {
        return majorRadius;
    }

    private final DoubleProperty minorRadius = new SimpleDoubleProperty(DEFAULT_MINOR_RADIUS){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getMinorRadius() {
        return minorRadius.get();
    }

    public final void setMinorRadius(double value) {
        minorRadius.set(value);
    }

    public DoubleProperty minorRadiusProperty() {
        return minorRadius;
    }
    
    private final DoubleProperty wireRadius = new SimpleDoubleProperty(DEFAULT_WIRE_RADIUS){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getWireRadius() {
        return wireRadius.get();
    }

    public final void setWireRadius(double value) {
        wireRadius.set(value);
    }

    public DoubleProperty wireRadiusProperty() {
        return wireRadius;
    }

    private final DoubleProperty length = new SimpleDoubleProperty(DEFAULT_LENGTH){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getLength() {
        return length.get();
    }

    public final void setLength(double value) {
        length.set(value);
    }

    public DoubleProperty lengthProperty() {
        return length;
    }

    private final DoubleProperty q = new SimpleDoubleProperty(DEFAULT_Q){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getQ() {
        return q.get();
    }

    public final void setQ(double value) {
        setLength(2d*Math.PI*Math.abs(value));
        q.set(value);
    }

    public DoubleProperty qProperty() {
        return q;
    }
    private final DoubleProperty p = new SimpleDoubleProperty();

    public double getP() {
        return p.get();
    }

    public void setP(double value) {
        p.set(value);
    }

    public DoubleProperty pProperty() {
        return p;
    }
    
    private final IntegerProperty lengthDivisions = new SimpleIntegerProperty(DEFAULT_LENGTH_DIVISIONS){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final int getLengthDivisions() {
        return lengthDivisions.get();
    }

    public final void setLengthDivisions(int value) {
        lengthDivisions.set(value);
    }

    public IntegerProperty lengthDivisionsProperty() {
        return lengthDivisions;
    }

    private final IntegerProperty wireDivisions = new SimpleIntegerProperty(DEFAULT_WIRE_DIVISIONS){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final int getWireDivisions() {
        return wireDivisions.get();
    }

    public final void setWireDivisions(int value) {
        wireDivisions.set(value);
    }

    public IntegerProperty wireDivisionsProperty() {
        return wireDivisions;
    }

    private final IntegerProperty lengthCrop = new SimpleIntegerProperty(DEFAULT_LENGTH_CROP){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };
    
    public final int getLengthCrop() {
        return lengthCrop.get();
    }

    public final void setLengthCrop(int value) {
        lengthCrop.set(value);
    }

    public IntegerProperty lengthCropProperty() {
        return lengthCrop;
    }

    private final IntegerProperty wireCrop = new SimpleIntegerProperty(DEFAULT_WIRE_CROP){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };
    
    public final int getWireCrop() {
        return wireCrop.get();
    }

    public final void setWireCrop(int value) {
        wireCrop.set(value);
    }

    public IntegerProperty wireCropProperty() {
        return wireCrop;
    }
    
    private final DoubleProperty tubeStartAngleOffset = new SimpleDoubleProperty(DEFAULT_START_ANGLE){

        @Override protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
    };

    public final double getTubeStartAngleOffset() {
        return tubeStartAngleOffset.get();
    }

    public void setTubeStartAngleOffset(double value) {
        tubeStartAngleOffset.set(value);
    }

    public DoubleProperty tubeStartAngleOffsetProperty() {
        return tubeStartAngleOffset;
    }
    private final DoubleProperty xOffset = new SimpleDoubleProperty(DEFAULT_X_OFFSET){

        @Override
        protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
        
    };

    public final double getxOffset() {
        return xOffset.get();
    }

    public void setxOffset(double value) {
        xOffset.set(value);
    }

    public DoubleProperty xOffsetProperty() {
        return xOffset;
    }
    private final DoubleProperty yOffset = new SimpleDoubleProperty(DEFAULT_Y_OFFSET){

        @Override
        protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
        
    };

    public final double getyOffset() {
        return yOffset.get();
    }

    public void setyOffset(double value) {
        yOffset.set(value);
    }

    public DoubleProperty yOffsetProperty() {
        return yOffset;
    }
    private final DoubleProperty zOffset = new SimpleDoubleProperty(DEFAULT_Z_OFFSET){

        @Override
        protected void invalidated() {
            if(mesh!=null){
                updateMesh();
            }
        }
        
    };

    public final double getzOffset() {
        return zOffset.get();
    }

    public void setzOffset(double value) {
        zOffset.set(value);
    }

    public DoubleProperty zOffsetProperty() {
        return zOffset;
    }
    
    private TriangleMesh createSpring(float majorRadius, float minorRadius, float wireRadius, float p, float q, float length, 
            int subDivLength, int subDivWire, int cropLength, int cropWire,
            float startAngle, float xOffset, float yOffset, float zOffset) {
 
        listVertices.clear();
        listTextures.clear();
        listFaces.clear();
        
        int numDivLength = subDivLength + 1-2*cropLength;
        int numDivWire = subDivWire + 1-2*cropWire;
        float pointX, pointY, pointZ;
        double a=wireRadius;
        
        knot = new KnotHelper(majorRadius, minorRadius, p, q);
        areaMesh.setWidth(knot.getLength());
        areaMesh.setHeight(polygonalSize(wireRadius));
        
        knot.calculateTrihedron(subDivLength);
        for (int t = cropLength; t <= subDivLength-cropLength; t++) {  // 0 - length
            for (int u = cropWire; u <= subDivWire-cropWire; u++) { // -Pi - +Pi
                if(cropWire>0 || (cropWire==0 && u<subDivWire)){
                    float du = (float) (((double)u)*2d*Math.PI / ((double)subDivWire));
                    double pol = polygonalSection(du);
                    float cu=(float)(a*pol*Math.cos(du)), su=(float)(a*pol*Math.sin(du)); 
                    listVertices.add(knot.getS(t, cu, su));
                }
            }
        }
        
        // Create texture coordinates
        createReverseTexCoords(subDivLength-2*cropLength,subDivWire-2*cropWire);
        
        // Create textures
        for (int t = cropLength; t < subDivLength-cropLength; t++) { // 0 - length
            for (int u = cropWire; u < subDivWire-cropWire; u++) { // -Pi - +Pi
                int p00 = (u-cropWire) + (t-cropLength)* numDivWire;
                int p01 = p00 + 1;
                int p10 = p00 + numDivWire;
                int p11 = p10 + 1;
                listTextures.add(new Point3D(p00,p01,p11));
                listTextures.add(new Point3D(p11,p10,p00));            
            }
        }
        
        // Create faces
        for (int t = cropLength; t < subDivLength-cropLength; t++) { // 0 - length
            for (int u = cropWire; u < subDivWire-cropWire; u++) { // -Pi - +Pi
                int p00 = (u-cropWire) + (t-cropLength)* (cropWire==0?subDivWire:numDivWire);
                int p01 = p00 + 1;
                int p10 = p00 + (cropWire==0?subDivWire:numDivWire);
                int p11 = p10 + 1;
                if(cropWire==0 && u==subDivWire-1){
                    p01-=subDivWire;
                    p11-=subDivWire;
                }
                listFaces.add(new Point3D(p00,p01,p11));
                listFaces.add(new Point3D(p11,p10,p00));            
            }
        }
        
        
//        for (int u = cropWire; u < subDivWire-cropWire; u++) { // -Pi - +Pi
//            for (int t = cropLength; t < subDivLength-cropLength; t++) { // 0 - length
//                int p00 = (u-cropWire) * numDivLength + (t-cropLength);
//                int p01 = p00 + 1;
//                if(cropLength==0 && t==subDivLength-1){
//                    p01-=subDivLength;
//                }
//                int p10 = p00 + numDivLength;
//                if(cropWire==0 && u==subDivWire-1){
//                    p10-=subDivWire*numDivLength;
//                }
//                int p11 = p10 + 1;
//                if(cropLength==0 && t==subDivLength-1){
//                    p11-=subDivLength;
//                }
//                listFaces.add(new Point3D(p00,p10,p11));
//                listFaces.add(new Point3D(p11,p01,p00));            
//            }
//        }
        return createMesh();
    }
    
    public Point3D getPositionAt(double t){
        return knot.getPositionAt(t);
    }
    
    public Point3D getTangentAt(double t){
        return knot.getTangentAt(t);
    }
    
    public float getTau(double t){
        return knot.getTau(t);
    }
    
    public float getKappa(double t){
        return knot.getKappa(t);
    }
}
