package com.study.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.study.exception.ExplosionException;

public class Board {
  private int lines;
  private int columns;
  private int mines;
  private final List<Field> fields = new ArrayList<>();

  public Board(int lines, int columns, int mines) {
    this.lines = lines;
    this.columns = columns;
    this.mines = mines;

    this.generateFields();
    this.bindNeighbors();
    this.raffleMines();
  }

  public void openField(int line, int column) {
    try {
      getField(line, column).ifPresent(field -> field.open());
    } catch (ExplosionException e) {
      fields.forEach(field -> field.setOpen(true));
      throw e;
    }
  }

  public void flagField(int line, int column) {
    getField(line, column).ifPresent(field -> field.swapFlagged());
  }

  private Optional<Field> getField(int line, int column) {
    return this.fields.parallelStream()
        .filter(field -> field.getLine() == line && field.getColumn() == column)
        .findFirst();
  }

  private void generateFields() {
    for (int line = 0; line < this.lines; line++) {
      for (int column = 0; column < this.columns; column++) {
        fields.add(new Field(line, column));
      }
    }
  }

  private void bindNeighbors() {
    for (Field c1 : fields) {
      for (Field c2 : fields) {
        c1.addNeighbor(c2);
      }
    }
  }

  private void raffleMines() {
    long quantityMine = 0;

    do {
      int random = (int) (Math.random() * fields.size());
      fields.get(random).mine();
      quantityMine = fields.stream().filter(field -> field.isMine()).count();
    } while (quantityMine < this.mines);
  }

  public boolean goalChieved() {
    return fields.stream().allMatch(field -> field.goalChieved());
  }

  public void reset() {
    this.fields.stream().forEach(field -> field.reset());
    this.raffleMines();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("   ");
    for (int i = 0; i < this.columns; i++) {
      sb.append(String.format(" %d ", i));
    }

    sb.append("\n");

    int i = 0;
    for (int line = 0, endLine = this.lines; line < endLine; line++) {
      sb.append(String.format(" %d ", line));
      for (int column = 0, endColumn = this.lines; column < endColumn; column++) {
        sb.append(String.format(" %s ", fields.get(i)));
        i++;
      }
      sb.append("\n");
    }

    return sb.toString();
  }

}
