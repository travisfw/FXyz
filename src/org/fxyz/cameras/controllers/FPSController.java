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
package org.fxyz.cameras.controllers;

import javafx.scene.input.KeyCode;
import org.fxyz.utils.AnimationPreference;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.fxyz.utils.MathUtils;

import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.SPACE;

public class FPSController extends CameraController {

  private final KeyCode strafeLeft, strafeRight, strafeBack, strafeForward;
  private boolean fwd, strafeL, strafeR, back, up, down, shift, mouseLookEnabled;
  private double speed = 1.0;
  private final double maxSpeed = 5.0, minSpeed = 1.0;


  public FPSController() {
    this(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D);
  }

  /**
   * For example, WASD.
   */
  public FPSController(KeyCode forward, KeyCode left, KeyCode back, KeyCode right) {
    super(true, AnimationPreference.TIMER);
    strafeLeft = left;
    strafeRight = right;
    strafeForward = forward;
    strafeBack = back;
  }

  @Override
  public void update() {

    if (fwd && !back) {
      moveForward();
    }
    if (strafeL) {
      strafeLeft();
    }
    if (strafeR) {
      strafeRight();
    }
    if (back && !fwd) {
      moveBack();
    }
    if (up && !down) {
      moveUp();
    }
    if (down && !up) {
      moveDown();
    }

  }

  @Override
  public void handleKeyEvent(KeyEvent event, boolean handle) {
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      KeyCode kc = event.getCode();
      if(kc == strafeForward)
        fwd = true;
      else if (kc == strafeBack)
        back = true;
      else if (kc == strafeLeft)
        strafeL = true;
      else if (kc == strafeRight)
        strafeR = true;
      else if (kc == SHIFT && !(up || down)) {
        shift = true;
        speed = maxSpeed;
      }
      else if (kc == SPACE) {
        if (!shift) {
          up = true;
        } else if (shift) {
          down = true;
        }
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      KeyCode kc = event.getCode();
      if (kc == strafeForward)
        fwd = false;
      else if (kc == strafeBack)
        back = false;
      else if (kc == strafeLeft)
        strafeL = false;
      else if (kc == strafeRight)
        strafeR = false;
      else if (kc == SHIFT) {
        speed = minSpeed;
        shift = false;
      } else if (kc == SPACE) {
        up = false;
        down = false;
      }
    }
  }

  @Override
  protected void handlePrimaryMouseDrag(MouseEvent event, Point2D dragDelta, double modifier) {
    if (!mouseLookEnabled) {
      t.setX(getPosition().getX());
      t.setY(getPosition().getY());
      t.setZ(getPosition().getZ());

      affine.setToIdentity();

      rotateY.setAngle(
          MathUtils.clamp(((rotateY.getAngle() + dragDelta.getX() * (1.0 * 0.25)) % 360 + 540) % 360 - 180, -360, 360)
      ); // horizontal
      rotateX.setAngle(
          MathUtils.clamp(((rotateX.getAngle() - dragDelta.getY() * (1.0 * 0.25)) % 360 + 540) % 360 - 180, -90, 90)
      ); // vertical

      affine.prepend(t.createConcatenation(rotateY.createConcatenation(rotateX)));
    }
  }

  @Override
  protected void handleMiddleMouseDrag(MouseEvent event, Point2D dragDelta, double modifier) {
    // do nothing for now
  }

  @Override
  protected void handleSecondaryMouseDrag(MouseEvent event, Point2D dragDelta, double modifier) {
    // do nothing for now
  }

  @Override
  protected void handleMouseMoved(MouseEvent event, Point2D moveDelta, double speed) {
    if (mouseLookEnabled) {
      t.setX(getPosition().getX());
      t.setY(getPosition().getY());
      t.setZ(getPosition().getZ());

      affine.setToIdentity();

      rotateY.setAngle(
          MathUtils.clamp(((rotateY.getAngle() + moveDelta.getX() * (speed * 0.05)) % 360 + 540) % 360 - 180, -360, 360)
      ); // horizontal
      rotateX.setAngle(
          MathUtils.clamp(((rotateX.getAngle() - moveDelta.getY() * (speed * 0.05)) % 360 + 540) % 360 - 180, -90, 90)
      ); // vertical

      affine.prepend(t.createConcatenation(rotateY.createConcatenation(rotateX)));

    }
  }


  @Override
  protected void handleScrollEvent(ScrollEvent event) {
    //do nothing for now, use for Zoom?
  }

  @Override
  protected double getSpeedModifier(KeyEvent event) {
    return speed;
  }

  @Override
  public Node getTransformableNode() {
    if (getCamera() != null) {
      return getCamera();
    } else {
      throw new UnsupportedOperationException("Must have a Camera");
    }
  }

  private void moveForward() {
    affine.setTx(getPosition().getX() + speed * getForwardMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * getForwardMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * getForwardMatrixRow().z);
  }

  private void strafeLeft() {
    affine.setTx(getPosition().getX() + speed * -getRightMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * -getRightMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * -getRightMatrixRow().z);
  }

  private void strafeRight() {
    affine.setTx(getPosition().getX() + speed * getRightMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * getRightMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * getRightMatrixRow().z);
  }

  private void moveBack() {
    affine.setTx(getPosition().getX() + speed * -getForwardMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * -getForwardMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * -getForwardMatrixRow().z);
  }

  private void moveUp() {
    affine.setTx(getPosition().getX() + speed * -getUpMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * -getUpMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * -getUpMatrixRow().z);
  }

  private void moveDown() {
    affine.setTx(getPosition().getX() + speed * getUpMatrixRow().x);
    affine.setTy(getPosition().getY() + speed * getUpMatrixRow().y);
    affine.setTz(getPosition().getZ() + speed * getUpMatrixRow().z);
  }

  public void setMouseLookEnabled(boolean b) {
    mouseLookEnabled = b;
  }

  @Override
  protected void handlePrimaryMouseClick(MouseEvent t) {
    //System.out.println("Primary Button Clicked!");
  }

  @Override
  protected void handleMiddleMouseClick(MouseEvent t) {
    //System.out.println("Middle Button Clicked!");
  }

  @Override
  protected void handleSecondaryMouseClick(MouseEvent t) {
    //System.out.println("Secondary Button Clicked!");
  }

  @Override
  protected void handlePrimaryMousePress(MouseEvent e) {

  }

  @Override
  protected void handleSecondaryMousePress(MouseEvent e) {

  }

  @Override
  protected void handleMiddleMousePress(MouseEvent e) {

  }

  @Override
  protected void updateTransition(double now) {

  }

}
