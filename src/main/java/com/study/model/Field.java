package com.study.model;

import java.util.ArrayList;
import java.util.List;

import com.study.exception.ExplosionException;

public class Field {
  private final int line;
  private final int column;
  private boolean isOpen;
  private boolean isMined;
  private boolean flagged;
  private List<Field> neighbors = new ArrayList<Field>();

  public Field(int line, int column) {
    this.line = line;
    this.column = column;
  }

  boolean addNeighbor(Field neighbor) {
    int neighborLine = neighbor.line;
    int neighborColumn = neighbor.column;

    if (neighborLine == this.line && neighborColumn == this.column) {
      return false;
    }

    int line = Math.abs(this.line - neighborLine);
    int column = Math.abs(this.column - neighborColumn);
    boolean NoisNeighbor = (line + column) > 2;

    if (NoisNeighbor) {
      return false;
    }

    this.neighbors.add(neighbor);

    return true;
  }

  void swapFlagged() {
    if (this.isOpen) {
      return;
    }

    this.flagged = !this.flagged;
  }

  boolean open() {
    if (this.isOpen || this.flagged) {
      return false;
    }

    if (this.isMined) {
      throw new ExplosionException();
    }

    if (this.neighborhoodSafe()) {
      this.neighbors.stream().forEach(neighbor -> neighbor.open());
    }

    this.isOpen = true;
    return true;
  }

  private boolean neighborhoodSafe() {
    return this.neighbors.stream().allMatch(neighbor -> !neighbor.isMined);
  }

  void mine() {
    this.isMined = true;
  }

  void setOpen(boolean isOpen) {
    this.isOpen = isOpen;
  }

  boolean isMine() {
    return this.isMined;
  }

  int getLine() {
    return this.line;
  }

  int getColumn() {
    return this.column;
  }

  public boolean goalChieved() {
    boolean unraveled = !this.isMined && this.isOpen;
    boolean protectedd = this.isMined && this.flagged;

    return unraveled || protectedd;
  }

  private long mineOnNeighborhood() {
    return neighbors.stream().filter(neighbor -> neighbor.isMined).count();
  }

  void reset() {
    this.isOpen = false;
    this.isMined = false;
    this.flagged = false;
  }

  @Override
  public String toString() {
    if (this.flagged) {
      return "X";
    }

    if (this.isOpen && this.isMined) {
      return "*";
    }

    if (this.isOpen && this.mineOnNeighborhood() > 0) {
      return Long.toString(this.mineOnNeighborhood());
    }

    if (this.isOpen) {
      return " ";
    }

    return "?";
  }

}
