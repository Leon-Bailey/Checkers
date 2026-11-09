import java.util.Scanner;
import java.util.ArrayList;
public class CheckersDemo
{
        // Declare Scanner + Initialize Checkers Board
        public static Scanner inp = new Scanner(System.in);
        public static int pieceRow = -1;
        public static char pieceColumn = 'Z';
        public static int rowIndex = -1;
        public static int columnIndex = -1;
        public static int piece = -1;
        public static boolean winCon = false;
        public static boolean playerOneTurn = true;
        public static int[][] checkersBoard =
        {
          {0, 1, 0, 1, 0, 1, 0, 1},
          {1, 0, 1, 0, 1, 0, 1, 0},
          {0, 1, 0, 1, 0, 1, 0, 1},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0, 0},
          {2, 0, 2, 0, 2, 0, 2, 0},
          {0, 2, 0, 2, 0, 2, 0, 2},
          {2, 0, 2, 0, 2, 0, 2, 0}
        };
        
        public static void main(String[] args)
        {
            playGame();
        }

        public static void playGame()
        {
            System.out.println("-----------Welcome to Checkers!-----------");
            System.out.println("Player 1 (1's) Goes First.\n\n");
            printBoard();
            while(winCon == false)
            {
                boolean succesfulTurn = false;
                while(!succesfulTurn)
                {
                    pickAPiece();
                    succesfulTurn = pieceMovement();
                }
                playerOneTurn = !playerOneTurn;
                System.out.println("\n\nNext Player's Turn-----------------------");
                winCon = checkForWin();
            }
        }
        
        // Print Checkers Board Method
        public static void printBoard()
        {
            System.out.print("    "); 
            for (int j = 0; j < checkersBoard[0].length; j++)
            {
                char colHeader = (char) ('A' + j);
                System.out.print(colHeader + "   ");
            }
            System.out.println();
    
            System.out.println("  ---------------------------------");
    
            for (int i = 0; i < checkersBoard.length; i++)
            {
                System.out.print((i + 1) + " | ");
        
                for (int j = 0; j < checkersBoard[i].length; j++)
                {
                    System.out.print(checkersBoard[i][j] + "   ");
                }
                System.out.println();
            }
        }
      
       // Piece Selection + Movement Methods
        public static int pickAPiece()
        {
            int selectedPiece = 0;
            boolean validPieceSelected = false;
            
            boolean forcedCapture = isForcedCaptureAvailable();
            if(forcedCapture)
            {
                System.out.println("FORCED CAPTURE: A jump is available. You MUST select a piece that can capture.");
            }
            while(!validPieceSelected)
            {
                System.out.print("Select a row (1-8): ");
                pieceRow = inp.nextInt();
                while(pieceRow < 1 || pieceRow > 8)
                {
                    System.out.println("Invalid Row Selection. Try Again.");
                    System.out.print("Select a row (1-8): ");
                    pieceRow = inp.nextInt();
                }
                
                System.out.print("Select a column (A-H): ");
                pieceColumn = (inp.next().toUpperCase().charAt(0));
                while(pieceColumn < 'A' || pieceColumn > 'H')
                {
                    System.out.println("Invalid Column Selection. Try Again.");
                    System.out.print("Select a column (A-H): ");
                    pieceColumn = (inp.next().toUpperCase().charAt(0));
                }
                
                rowIndex = pieceRow - 1;
                columnIndex = pieceColumn - 'A';
                
                selectedPiece = checkersBoard[rowIndex][columnIndex];
                
                if(playerOneTurn && (selectedPiece == 1 || selectedPiece == 3))
                {
                    validPieceSelected = true;
                }
                else if(!playerOneTurn && (selectedPiece == 2 || selectedPiece == 4))
                {
                    validPieceSelected = true;
                }
                else
                {
                    if(playerOneTurn)
                    {
                        System.out.println("Invalid Selection. It is Player 1's turn. Select a valid piece (1 or 3).");
                    }
                    else
                    {
                        System.out.println("Invalid Selection. It is Player 2's turn. Select a valid piece (2 or 4).");
                    }
                }
                if(validPieceSelected && forcedCapture)
                {
                    if(!canPieceJump(rowIndex, columnIndex, selectedPiece))
                    {
                        System.out.println("Invalid Selection. You must choose a piece that can capture.");
                        validPieceSelected = false;
                    }
                }
            }
            return checkersBoard[rowIndex][columnIndex];
        }
        
        
        public static boolean pieceMovement()
        {
            piece = checkersBoard[rowIndex][columnIndex];
            
            System.out.print("Move piece " + pieceColumn + pieceRow + "?\n");
            if(piece != 3 && piece != 4)
            {
                int rowIndexSelect = (piece == 2) ? rowIndex - 1  : rowIndex + 1;
                
                // Set board parameters
                
                if (rowIndexSelect < 0 || rowIndexSelect >= 8) 
                {
                    System.out.println("This piece has reached the end of the board and cannot move further.");
                    return false;
                }
            
                // Insert pieceCapture Method 
                
                boolean captured = pieceCapture();
                if(captured == true)
                {
                    return true;
                }
                
                // Set up boolean statements that check for possible moves
                
                boolean leftMovement = false;
                boolean rightMovement = false;
                
                if( (pieceColumn != 'A') && (checkersBoard[rowIndexSelect][columnIndex - 1] == 0))
                {
                    leftMovement = true;
                }
                
                if((pieceColumn != 'H') && (checkersBoard[rowIndexSelect][columnIndex + 1] == 0))
                {
                    rightMovement = true;
                }
                // Print and do movement options
                
                if(!leftMovement && !rightMovement)
                {
                    System.out.println("No possible moves with this piece. Select another.");
                    return false;
                }
                else
                {
                    System.out.println("Possible Move Options For Piece " + pieceColumn + pieceRow + ": ");
                    if(leftMovement && rightMovement)
                    {
                        System.out.println("DL (Diagnol Left) and DR (Diagnol Right)");
                        System.out.println("Select a Move (DL/DR): ");
                        String userMove = inp.next().toUpperCase();
                        if(userMove.equals("DL"))
                        {
                            checkersBoard[rowIndex][columnIndex] = 0;
                            checkersBoard[rowIndexSelect][columnIndex - 1] = piece;
                            
                            rowIndex = rowIndexSelect;
                            columnIndex = columnIndex - 1;
                            kingPromotion();
                            
                            printBoard();
                        }
                        else if(userMove.equals("DR"))
                        {
                            checkersBoard[rowIndex][columnIndex] = 0;
                            checkersBoard[rowIndexSelect][columnIndex + 1] = piece;
                            
                            rowIndex = rowIndexSelect;
                            columnIndex = columnIndex + 1;
                            kingPromotion();
                            
                            printBoard();
                        }
                        else
                        {
                            System.out.println("Invalid Selection. Try Again");
                            return false;
                        }
                    }
                    else if(leftMovement && !rightMovement)
                    {
                        System.out.println("DL (Diagnol Left)");
                        System.out.println("Select a Move (DL): ");
                        String userMove = inp.next().toUpperCase();
                        if(userMove.equals("DL"))
                        {
                            checkersBoard[rowIndex][columnIndex] = 0;
                            checkersBoard[rowIndexSelect][columnIndex - 1] = piece;
                            
                            rowIndex = rowIndexSelect;
                            columnIndex = columnIndex - 1;
                            kingPromotion();
                            
                            printBoard();
                        }
                        else
                        {
                            System.out.println("Invalid Selection. Try Again");
                            return false;
                        }
                    }
                    else
                    {
                        System.out.println("DR (Diagnol Right)");
                        System.out.println("Select a Move (DR): ");
                        String userMove = inp.next().toUpperCase();
                        if(userMove.equals("DR"))
                        {
                            checkersBoard[rowIndex][columnIndex] = 0;
                            checkersBoard[rowIndexSelect][columnIndex + 1] = piece;
                            
                            rowIndex = rowIndexSelect;
                            columnIndex = columnIndex + 1;
                            kingPromotion();
                            
                            printBoard();
                        }
                        else
                        {
                            System.out.println("Invalid Selection. Try Again");
                            return false;
                        }
                    }
                }
                return true;
            }
            else
            {
                boolean madeKingCapture = kingPieceCapture();
                if (madeKingCapture)
                {
                    return true;
                }
        
                // Check for available standard diagonal moves in all 4 directions
                boolean upLeft = false;
                boolean upRight = false;
                boolean downLeft = false;
                boolean downRight = false;
                
                if (rowIndex - 1 >= 0 && columnIndex - 1 >= 0 && checkersBoard[rowIndex - 1][columnIndex - 1] == 0) upLeft = true;
                if (rowIndex - 1 >= 0 && columnIndex + 1 < 8 && checkersBoard[rowIndex - 1][columnIndex + 1] == 0) upRight = true;
                if (rowIndex + 1 < 8 && columnIndex - 1 >= 0 && checkersBoard[rowIndex + 1][columnIndex - 1] == 0) downLeft = true;
                if (rowIndex + 1 < 8 && columnIndex + 1 < 8 && checkersBoard[rowIndex + 1][columnIndex + 1] == 0) downRight = true;
            
                if (!upLeft && !upRight && !downLeft && !downRight)
                {
                    System.out.println("No possible moves with this King. Select another.");
                    return false;
                }
                   ArrayList<String> validMoves = new ArrayList<>();
                   System.out.print("Possible Move Options For King " + pieceColumn + pieceRow + ": ");
                
                if (upLeft) { validMoves.add("UL"); System.out.print("UL (Up-Left) "); }
                if (upRight) { validMoves.add("UR"); System.out.print("UR (Up-Right) "); }
                if (downLeft) { validMoves.add("DL"); System.out.print("DL (Down-Left) "); }
                if (downRight) { validMoves.add("DR"); System.out.print("DR (Down-Right) "); }
                    
                System.out.println("\nSelect a Move (" + String.join("/", validMoves) + "): ");
                String userMove = inp.next().toUpperCase();
            
                // Execute the chosen move if it is valid
                if (validMoves.contains(userMove))
                {
                    checkersBoard[rowIndex][columnIndex] = 0; 
            
                    if (userMove.equals("UL"))
                    {
                        checkersBoard[rowIndex - 1][columnIndex - 1] = piece;
                        rowIndex -= 1; 
                        columnIndex -= 1;
                    }
                    else if (userMove.equals("UR")) 
                    {
                        checkersBoard[rowIndex - 1][columnIndex + 1] = piece;
                        rowIndex -= 1; 
                        columnIndex += 1;
                    }
                    else if (userMove.equals("DL"))
                    {
                        checkersBoard[rowIndex + 1][columnIndex - 1] = piece;
                        rowIndex += 1; 
                        columnIndex -= 1;
                    }
                    else if (userMove.equals("DR")) 
                    {
                        checkersBoard[rowIndex + 1][columnIndex + 1] = piece;
                        rowIndex += 1; 
                        columnIndex += 1;
                    }
                    printBoard();
                    return true;
                }
                else
                {
                    System.out.println("Invalid Selection. Try Again.");
                    return false;
                }
            }
        }
        
        public static boolean checkForWin()
        {
            int playerOnePieces = 0;
            int playerTwoPieces = 0;
        
            // Count all pieces on the board
            for (int i = 0; i < checkersBoard.length; i++)
            {
                for (int j = 0; j < checkersBoard[i].length; j++)
                {
                    int boardPieces = checkersBoard[i][j];
        
                    if (boardPieces == 1 || boardPieces == 3)
                    {
                        playerOnePieces++;
                    }
                    else if (boardPieces == 2 || boardPieces == 4)
                    {
                        playerTwoPieces++;
                    }
                }
            }
        
            // Check whether a player has run out of pieces
            if (playerOnePieces == 0)
            {
                winCon = true;
                System.out.println("Game Over. Player 2 Wins.");
            }
            else if (playerTwoPieces == 0)
            {
                winCon = true;
                System.out.println("Game Over. Player 1 Wins.");
            }
        
            // Check whether the next player has any legal moves
            else if (!hasLegalMoves(playerOneTurn ? 1 : 2))
            {
                winCon = true;
        
                if (playerOneTurn)
                {
                    System.out.println("Game Over. Player 2 Wins. Player 1 has no legal moves.");
                }
                else
                {
                    System.out.println("Game Over. Player 1 Wins. Player 2 has no legal moves.");
                }
            }
            else
            {
                winCon = false;
            }
        
            return winCon;
        }
        
        public static boolean hasLegalMoves(int player)
        {
            // Scan the entire board
            for (int r = 0; r < 8; r++)
            {
                for (int c = 0; c < 8; c++)
                {
                    int p = checkersBoard[r][c];
        
                    // Skip pieces that don't belong to this player
                    if (player == 1 && p != 1 && p != 3)
                    {
                        continue;
                    }
                    else if (player == 2 && p != 2 && p != 4)
                    {
                        continue;
                    }
        
                    // Determine possible movement directions
                    int[] rowDirections;
        
                    if (p == 1)
                    {
                        rowDirections = new int[]{1};
                    }
                    else if (p == 2)
                    {
                        rowDirections = new int[]{-1};
                    }
                    else
                    {
                        // Kings can move in both directions
                        rowDirections = new int[]{-1, 1};
                    }
        
                    // Check both diagonal directions
                    for (int dr : rowDirections)
                    {
                        for (int dc : new int[]{-1, 1})
                        {
                            int nextRow = r + dr;
                            int nextCol = c + dc;
        
                            // Make sure the adjacent square is on the board
                            if (nextRow < 0 || nextRow >= 8 ||
                                nextCol < 0 || nextCol >= 8)
                            {
                                continue;
                            }
        
                            int adjacentPiece = checkersBoard[nextRow][nextCol];
        
                            // Check for normal movement
                            if (adjacentPiece == 0)
                            {
                                return true;
                            }
        
                            // Check for captures
                            boolean opponentPiece;
        
                            if (player == 1)
                            {
                                opponentPiece = (adjacentPiece == 2 ||
                                                 adjacentPiece == 4);
                            }
                            else
                            {
                                opponentPiece = (adjacentPiece == 1 ||
                                                 adjacentPiece == 3);
                            }
        
                            int jumpRow = r + (dr * 2);
                            int jumpCol = c + (dc * 2);
        
                            if (opponentPiece &&
                                jumpRow >= 0 && jumpRow < 8 &&
                                jumpCol >= 0 && jumpCol < 8 &&
                                checkersBoard[jumpRow][jumpCol] == 0)
                            {
                                return true;
                            }
                        }
                    }
                }
            }
            // No legal moves were found
            return false;
        }
        
        public static boolean kingPromotion()
        {
            // Player 1 (pieces '1') start at the top and promote at the bottom (Row 7)
            if (piece == 1 && rowIndex == 7) 
            {
                checkersBoard[rowIndex][columnIndex] = 3;
                System.out.println("\n*** KING PROMOTION! Player 1's piece is now a King! ***\n");
                return true;
            } 
            // Player 2 (pieces '2') start at the bottom and promote at the top (Row 0)
            else if (piece == 2 && rowIndex == 0) 
            {
                checkersBoard[rowIndex][columnIndex] = 4;
                System.out.println("\n*** KING PROMOTION! Player 2's piece is now a King! ***\n");
                return true;
            }
            return false;
        }
        
        public static boolean kingPieceCapture()
        {
            boolean kingCapture = false;
            
            while(true)
            {
                boolean fwdLeftCapture = false;
                boolean fwdRightCapture = false;
                boolean bckLeftCapture = false;
                boolean bckRightCapture = false;
                
                int [] oppositeKingPiece = (piece == 3) ? new int []{2, 4} : new int []{1, 3};
                
                int fwdDir = (piece == 3) ? 1 : -1;
                int bckDir = -fwdDir;
                
                int forwardRowIndex = rowIndex + fwdDir;
                int backwardRowIndex = rowIndex + bckDir;
                int forwardJumpRowIndex = rowIndex + (fwdDir * 2);
                int backwardJumpRowIndex = rowIndex + (bckDir * 2);
                
                if(forwardJumpRowIndex >= 0 && forwardJumpRowIndex <= 7)
                {
                    // Right Capture
                    if(columnIndex + 2 <= 7)
                    {
                        for(int l = 0; l < oppositeKingPiece.length; l++)
                        {
                            if((checkersBoard[forwardRowIndex][columnIndex + 1] == oppositeKingPiece[l]) && (checkersBoard[forwardJumpRowIndex][columnIndex + 2] == 0))
                            {
                                fwdRightCapture = true;
                            }
                        }
                    }
                    
                    // Left Capture
                    if(columnIndex - 2 >= 0)
                    {
                        for(int l = 0; l < oppositeKingPiece.length; l++)
                        {
                            if((checkersBoard[forwardRowIndex][columnIndex - 1] == oppositeKingPiece[l]) && (checkersBoard[forwardJumpRowIndex][columnIndex - 2] == 0))
                            {
                                fwdLeftCapture = true;
                            }
                        }
                    }
                }
                
                if(backwardJumpRowIndex >= 0 && backwardJumpRowIndex <= 7)
                {
                    // Right Capture
                    if(columnIndex + 2 <= 7) 
                    {
                        for(int l = 0; l < oppositeKingPiece.length; l++)
                        {
                            if((checkersBoard[backwardRowIndex][columnIndex + 1] == oppositeKingPiece[l]) && (checkersBoard[backwardJumpRowIndex][columnIndex + 2] == 0))
                            {
                                bckRightCapture = true;
                            }
                        }
                    }
                    
                    // Left Capture
                    if(columnIndex - 2 >= 0) 
                    { 
                        for(int l = 0; l < oppositeKingPiece.length; l++)
                        {
                            if((checkersBoard[backwardRowIndex][columnIndex - 1] == oppositeKingPiece[l]) && (checkersBoard[backwardJumpRowIndex][columnIndex - 2] == 0))
                            {
                                bckLeftCapture = true;
                            }
                        }
                    }
                }
                
                int fwdDisplayRow = forwardJumpRowIndex + 1;
                int bckDisplayRow = backwardJumpRowIndex + 1;
                
                String fwdRightPiece = "" + (char)(pieceColumn + 2) + fwdDisplayRow;
                String fwdLeftPiece = "" + (char)(pieceColumn - 2) + fwdDisplayRow;
                String bckRightPiece = "" + (char)(pieceColumn + 2) + bckDisplayRow;
                String bckLeftPiece = "" + (char)(pieceColumn - 2) + bckDisplayRow;
                
                // Add available captures to a list
                ArrayList<String> validCaptures = new ArrayList<>();
                
                if (fwdRightCapture) validCaptures.add(fwdRightPiece);
                if (fwdLeftCapture) validCaptures.add(fwdLeftPiece);
                if (bckRightCapture) validCaptures.add(bckRightPiece);
                if (bckLeftCapture) validCaptures.add(bckLeftPiece);
                
                // If no captures are available, break the while loop
                if (validCaptures.isEmpty())
                {
                    break; 
                }
                
                // Display options to the user
                System.out.println("Pieces You Can Capture: " + String.join(", ", validCaptures));
                System.out.println("Which piece would you like to capture: ");
                String kingCaptureSelect = inp.next().toUpperCase();
                
                // Validate input against the list
                while (!validCaptures.contains(kingCaptureSelect))
                {
                    System.out.println("Invalid selection. Try again.");
                    kingCaptureSelect = inp.next().toUpperCase();
                }
                
                // Execute the chosen capture mechanism
                if (kingCaptureSelect.equals(fwdRightPiece))
                {
                    System.out.println("Executing Forward-Right Capture...");
                    
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[forwardRowIndex][columnIndex + 1] = 0;
                    checkersBoard[forwardJumpRowIndex][columnIndex + 2] = piece;
                    
                    // Update Coordinates
                    rowIndex = forwardJumpRowIndex;
                    columnIndex = columnIndex + 2;
                    pieceColumn = (char)(pieceColumn + 2);
                    
                }
                else if (kingCaptureSelect.equals(fwdLeftPiece))
                {
                    System.out.println("Executing Forward-Left Capture...");
                    
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[forwardRowIndex][columnIndex - 1] = 0;
                    checkersBoard[forwardJumpRowIndex][columnIndex - 2] = piece;
                        
                    // Update Coordinates
                    rowIndex = forwardJumpRowIndex;
                    columnIndex = columnIndex - 2;
                    pieceColumn = (char)(pieceColumn - 2);
                }
                else if (kingCaptureSelect.equals(bckRightPiece))
                {
                    System.out.println("Executing Backward-Right Capture...");
                    
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[backwardRowIndex][columnIndex + 1] = 0;
                    checkersBoard[backwardJumpRowIndex][columnIndex + 2] = piece;
                    
                    // Update Coordinates
                    rowIndex = backwardJumpRowIndex;
                    columnIndex = columnIndex + 2;
                    pieceColumn = (char)(pieceColumn + 2);
                }
                else if (kingCaptureSelect.equals(bckLeftPiece))
                {
                    System.out.println("Executing Backward-Left Capture...");
                    
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[backwardRowIndex][columnIndex - 1] = 0;
                    checkersBoard[backwardJumpRowIndex][columnIndex - 2] = piece;
                    
                    // Update Coordinates
                    rowIndex = backwardJumpRowIndex;
                    columnIndex = columnIndex - 2;
                    pieceColumn = (char)(pieceColumn - 2);
                }
                printBoard();
                kingCapture = true;
            }
            return kingCapture;
        }
        
        public static boolean pieceCapture()
        {
            boolean madeCapture = false;
            
            while(true)
            {
                boolean leftCapture = false;
                boolean rightCapture = false;
                
                int [] oppositePiece = (piece == 2) ? new int []{1, 3} : new int []{2, 4};
                int rowIndexSelect = (piece == 2) ? rowIndex - 1  : rowIndex + 1;
                int jumpRowIndexSelect = (piece == 2) ? rowIndex - 2  : rowIndex + 2;
                
                // Set up boolean conditionals that check if capturing a piece is possible
                if(jumpRowIndexSelect >= 0 && jumpRowIndexSelect <= 7)
                {
                    // Right Capture
                    if(columnIndex + 2 <= 7)
                    {
                        for(int l = 0; l < oppositePiece.length; l++)
                        {
                            if((checkersBoard[rowIndexSelect][columnIndex + 1] == oppositePiece[l]) && (checkersBoard[jumpRowIndexSelect][columnIndex + 2] == 0))
                            {
                                rightCapture = true;
                            }
                        }
                    }
                    
                    // Left Capture
                    if(columnIndex - 2 >= 0)
                    {
                        for(int l = 0; l < oppositePiece.length; l++)
                        {
                            if((checkersBoard[rowIndexSelect][columnIndex - 1] == oppositePiece[l]) && (checkersBoard[jumpRowIndexSelect][columnIndex - 2] == 0))
                            {
                                leftCapture = true;
                            }
                        }
                    }
                }
                
                if(!rightCapture && !leftCapture)
                {
                    break;
                }
                
                madeCapture = true;
                
                // Display capture possibilities and execute them
                char leftJumpColumn = (char)(pieceColumn - 2);
                char rightJumpColumn = (char)(pieceColumn + 2);
                int displayRow = jumpRowIndexSelect + 1;
                
                if(rightCapture && leftCapture)
                {
                    // Print out capture options + start capture process
                    System.out.println("You can capture pieces: " + leftJumpColumn + displayRow + " or " + rightJumpColumn + displayRow);
                    System.out.println("Which piece would you like to capture: ");
                    String userCaptureSelection = inp.next().toUpperCase();
                    
                    // Catch Invalid Input
                    String leftOption = ("" + leftJumpColumn + displayRow);
                    String rightOption = ("" + rightJumpColumn + displayRow);
                    while(!userCaptureSelection.equals(leftOption) && !userCaptureSelection.equals(rightOption))
                    {
                        System.out.println("Invalid selection. Try again.");
                        userCaptureSelection = inp.next().toUpperCase();
                    }
                    
                    if(userCaptureSelection.equals(leftOption))
                    {
                        // Execute left jump
                        checkersBoard[rowIndex][columnIndex] = 0;
                        checkersBoard[rowIndexSelect][columnIndex - 1] = 0;
                        checkersBoard[jumpRowIndexSelect][columnIndex - 2] = piece;
                        
                        // Update Coordinates
                        rowIndex = jumpRowIndexSelect;
                        columnIndex = columnIndex - 2;
                        pieceColumn = leftJumpColumn;
                        
                        if(kingPromotion())
                        {
                            break;
                        }
                    }
                    else
                    {
                        // Execute right jump
                        checkersBoard[rowIndex][columnIndex] = 0;
                        checkersBoard[rowIndexSelect][columnIndex + 1] = 0;
                        checkersBoard[jumpRowIndexSelect][columnIndex + 2] = piece;
                        printBoard();
                        // Update Coordinates
                        rowIndex = jumpRowIndexSelect;
                        columnIndex = columnIndex + 2;
                        pieceColumn = rightJumpColumn;
                        
                        if(kingPromotion())
                        {
                            break;
                        }
                    }
                }
                else if(rightCapture && !leftCapture)
                {
                    System.out.println("You can capture piece: " + rightJumpColumn + displayRow);
                    System.out.println("Executing Jump...");
                    // Execute right jump
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[rowIndexSelect][columnIndex + 1] = 0;
                    checkersBoard[jumpRowIndexSelect][columnIndex + 2] = piece;
                    // Update Coordinates
                    rowIndex = jumpRowIndexSelect;
                    columnIndex = columnIndex + 2;
                    pieceColumn = rightJumpColumn;
                    
                    if(kingPromotion())
                    {
                        break;
                    }
                }
                else
                {
                    System.out.println("You can capture piece: " + leftJumpColumn + displayRow);
                    System.out.println("Executing Jump...");
                    // Execute left jump
                    checkersBoard[rowIndex][columnIndex] = 0;
                    checkersBoard[rowIndexSelect][columnIndex - 1] = 0;
                    checkersBoard[jumpRowIndexSelect][columnIndex - 2] = piece;
                    printBoard();
                    // Update Coordinates
                    rowIndex = jumpRowIndexSelect;
                    columnIndex = columnIndex - 2;
                    pieceColumn = leftJumpColumn;
                    
                    if(kingPromotion())
                    {
                        break;
                    }
                }
                printBoard();
                System.out.println("Checking for double jumps...");
            }
            return madeCapture;
        }
        
        public static boolean canPieceJump(int r, int c, int p)
        {
            // Determine opponent's pieces and allowed movement directions
            int[] rowDirections;
        
            if (p == 1)
            {
                rowDirections = new int[]{1};
            }
            else if (p == 2)
            {
                rowDirections = new int[]{-1};
            }
            else if (p == 3 || p == 4)
            {
                rowDirections = new int[]{-1, 1};
            }
            else
            {
                return false;
            }
        
            for (int dr : rowDirections)
            {
                for (int dc : new int[]{-1, 1})
                {
                    int middleRow = r + dr;
                    int middleCol = c + dc;
        
                    int jumpRow = r + (dr * 2);
                    int jumpCol = c + (dc * 2);
        
                    // Check that the jump destination is on the board
                    if (jumpRow >= 0 && jumpRow < 8 &&
                        jumpCol >= 0 && jumpCol < 8)
                    {
                        int middlePiece = checkersBoard[middleRow][middleCol];
        
                        // Determine whether the middle piece belongs to opponent
                        boolean opponentPiece;
        
                        if (p == 1 || p == 3)
                        {
                            opponentPiece = (middlePiece == 2 || middlePiece == 4);
                        }
                        else
                        {
                            opponentPiece = (middlePiece == 1 || middlePiece == 3);
                        }
        
                        if (opponentPiece && checkersBoard[jumpRow][jumpCol] == 0)
                        {
                            return true;
                        }
                    }
                }
            }
        
            return false;
        }
        
        public static boolean isForcedCaptureAvailable() 
        {
            // Scans the whole board for the current player's pieces
            for (int r = 0; r < 8; r++) 
            {
                for (int c = 0; c < 8; c++) 
                {
                    int p = checkersBoard[r][c];
                    if (playerOneTurn && (p == 1 || p == 3)) 
                    {
                        if (canPieceJump(r, c, p)) return true;
                    } 
                    else if (!playerOneTurn && (p == 2 || p == 4)) 
                    {
                        if (canPieceJump(r, c, p)) return true;
                    }
                }
            }
            return false;
        }
        
}