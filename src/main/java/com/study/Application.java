package com.study;

import com.study.model.Board;
import com.study.view.BoardConsole;

public class Application {
  public static void main(String[] args) {
    Board board = new Board(6, 6, 6);
    BoardConsole.create(board);
  }
}
