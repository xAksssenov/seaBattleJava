import java.util.Random;
import java.util.Scanner;

public class Main {
    public enum FieldCell {
        ALIVE_SHIP('K'),
        DEAD_SHIP('X'),
        MISS('O'),
        EMPTY('.');

        private final char value;

        FieldCell(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    public enum Player {
        USER("Игрок"),
        COMP("Компьютер");
        private final String value;

        Player(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static void main(String[] args) {

        //region Переменные + рандом
        Random random = new Random();

        FieldCell[][] compField, userField;
        Player currentPlayer, winPlayer = null;

        int filedSize = 0;
        int countCompShips, countUserShips;

        boolean playGame = true;
        //endregion

        System.out.println("Добро пожаловать в морской бой против компьютера: ");

        //region Ввод размера поля
        boolean inputResult;
        do {
            System.out.println("Пожалуйста введите размер поля игры (от 3 до 10): ");
            inputResult = true;

            try {
                Scanner scanner = new Scanner(System.in);
                filedSize = scanner.nextInt();

                if (filedSize < 3 || filedSize > 10) {
                    throw new Exception();
                }
            } catch (Exception e) {
                inputResult = false;
                System.out.println("Ошибка. Введите число в заданном диапазоне: ");
            }
        } while (!inputResult);
        //endregion

        //region Создание полей массива
        compField = new FieldCell[filedSize][filedSize];
        userField = new FieldCell[filedSize][filedSize];

        countCompShips = countUserShips = filedSize;

        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                compField[i][j] = FieldCell.EMPTY;
                userField[i][j] = FieldCell.EMPTY;
            }
        }
        //endregion

        //region Рандомная расстановка кораблей
        for (int k = 0; k < countCompShips; k++) {
            int iShip, jShip;
            do {
                iShip = random.nextInt(filedSize);
                jShip = random.nextInt(filedSize);
            } while (compField[iShip][jShip] != FieldCell.EMPTY);

            compField[iShip][jShip] = FieldCell.ALIVE_SHIP;
        }

        for (int k = 0; k < countUserShips; k++) {
            int iShip, jShip;
            do {
                iShip = random.nextInt(filedSize);
                jShip = random.nextInt(filedSize);
            } while (userField[iShip][jShip] != FieldCell.EMPTY);

            userField[iShip][jShip] = FieldCell.ALIVE_SHIP;
        }

        //endregion

        //region Рандомирование хода (игрок или компьютер)
        if (random.nextInt(1000 + 1) > 500) {
            currentPlayer = Player.USER;
            System.out.println("Первым ходит игрок: ");
        } else {
            currentPlayer = Player.COMP;
            System.out.println("Первым ходит компьютер: ");
        }
        System.out.println("Для продолжения нажмите <Enter>");
        new Scanner(System.in).nextLine();
        //endregion

        //region Игровой цикл
        while (playGame) {
            //region очистка экрана
            for (int i = 0; i < 50; i++) {
                System.out.println();
            } //endregion

            //region Вывод поля компьютера
            System.out.println("Поле компьютера: ");
            for (int i = 0; i < filedSize; i++) {
                for (int j = 0; j < filedSize; j++) {
                    if (compField[i][j] == FieldCell.ALIVE_SHIP) {
                        System.out.print(FieldCell.EMPTY.getValue());
                    } else {
                        System.out.print(compField[i][j].getValue());
                    }
                }
                System.out.println();
            } //endregion

            //region Вывод поля игрока
            System.out.println("Поле игрока: ");
            for (int i = 0; i < filedSize; i++) {
                for (int j = 0; j < filedSize; j++) {
                    System.out.print(userField[i][j].getValue());
                }
                System.out.println();
            }
            //endregion

            //region выстрел (игрока или компьютера)
            if (currentPlayer == Player.COMP) {
                System.out.println("Выстрел компьютера. Для продолжения нажмите <Enter>");
                new Scanner(System.in).nextLine();

                int iShoot, jShoot;

                do {
                    iShoot = random.nextInt(filedSize);
                    jShoot = random.nextInt(filedSize);
                } while (userField[iShoot][jShoot] == FieldCell.DEAD_SHIP || userField[iShoot][jShoot] == FieldCell.MISS);

                if (userField[iShoot][jShoot] == FieldCell.EMPTY) {
                    userField[iShoot][jShoot] = FieldCell.MISS;
                    currentPlayer = Player.USER;
                } else if (userField[iShoot][jShoot] == FieldCell.ALIVE_SHIP) {
                    userField[iShoot][jShoot] = FieldCell.DEAD_SHIP;
                    countUserShips -= 1;
                }
            } else if (currentPlayer == Player.USER) {
                System.out.println("Выстрел игрока. ");

                int iShoot = 0, jShoot = 0;
                boolean Shoot;

                do {
                    System.out.printf("Введите строчку для выстрела от %d до %d: %n", 1, filedSize);
                    Shoot = true;

                    try {
                        Scanner scanner = new Scanner(System.in);
                        iShoot = scanner.nextInt() - 1;

                        if (iShoot + 1 > filedSize || iShoot < 0) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        Shoot = false;
                    }

                    System.out.printf("Введите столбец для выстрела от %d до %d: %n", 1, filedSize);

                    try {
                        Scanner scanner = new Scanner(System.in);
                        jShoot = scanner.nextInt() - 1;

                        if (jShoot + 1 > filedSize || jShoot < 0) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        Shoot = false;
                    }

                } while (!Shoot);

                if (compField[iShoot][jShoot] == FieldCell.EMPTY) {
                    compField[iShoot][jShoot] = FieldCell.MISS;
                    currentPlayer = Player.COMP;
                } else if (compField[iShoot][jShoot] == FieldCell.ALIVE_SHIP) {
                    compField[iShoot][jShoot] = FieldCell.DEAD_SHIP;
                    countCompShips -= 1;
                }
            } //endregion

            //region Проверка выигрыша одного из игроков
            if (countCompShips == 0) {
                winPlayer = Player.USER;
                playGame = false;
            } else if (countUserShips == 0) {
                winPlayer = Player.COMP;
                playGame = false;
            }
            //endregion
        }
        //endregion

        //region Очистка экрана
        for (int i = 0; i < 50; i++) {
            System.out.println();
        } //endregion

        //region Вывод поля компьютера
        System.out.println("Поле компьютера: ");
        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                System.out.print(compField[i][j].getValue());
            }
            System.out.println();
        } //endregion

        //region Вывод поля игрока
        System.out.println("Поле игрока: ");
        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                System.out.print(userField[i][j].getValue());
            }
            System.out.println();
        }

        //endregion

        //region Вывод победителя

        System.out.println("Победил: " + winPlayer.getValue());

        //endregion

    }
}
