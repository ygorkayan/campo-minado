package com.study.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import com.study.exception.ExitException;
import com.study.exception.ExplosionException;
import com.study.model.Board;

public class BoardConsole {
  private Board board;
  private Scanner scanner = new Scanner(System.in);

  private BoardConsole(Board board) {
    this.board = board;
    this.startGame();
  }

  public static void create(Board board) {
    new BoardConsole(board);
  }

  private void startGame() {
    try {
      boolean continue2 = true;
      while (continue2) {
        this.circleGame();

        System.out.println("Do you want play again ? (S/n)");
        String response = this.scanner.nextLine();

        if (response.equalsIgnoreCase("n")) {
          continue2 = false;
        } else {
          this.board.reset();
        }

      }

    } catch (ExitException e) {
      System.out.println("Bye!!!");
    } finally {
      this.scanner.close();
    }
  }

  private void circleGame() {
    try {
      while (!board.goalChieved()) {
        System.out.println(this.board);

        Iterator<Integer> e = this.catchTypedValue("Typed (line, column): ");

        System.out.print("1 - Open, 2 - Flag/Unflagged:  ");
        String openOrFlag = this.scanner.nextLine();
        System.out.println();

        if (openOrFlag.equalsIgnoreCase("1")) {
          this.board.openField(e.next(), e.next());
        }
        if (openOrFlag.equalsIgnoreCase("2")) {
          this.board.flagField(e.next(), e.next());
        }
      }

      System.out.println(this.board);
      System.out.println("You winner!");
    } catch (ExplosionException e) {
      System.out.println(this.board);
      System.out.println("You lost!!");
    }

  }

  private Iterator<Integer> catchTypedValue(String text) {
    System.out.print(text);
    String typed = this.scanner.nextLine();

    if (typed.equalsIgnoreCase("exit")) {
      throw new ExitException();
    }

    return Arrays.stream(typed.split(","))
        .map(value -> Integer.parseInt(value.trim()))
        .iterator();
  }
}
