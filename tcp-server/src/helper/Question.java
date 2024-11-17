/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author admin
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DatabaseConnection;

public class Question {
    
    public static String renQuestion() {
        List<String> wordsList = new ArrayList<>();
        String query = "SELECT word FROM words ORDER BY RAND() LIMIT 5"; // Lấy 5 từ ngẫu nhiên

        // Lấy kết nối tới cơ sở dữ liệu
        Connection connection = DatabaseConnection.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Duyệt qua các kết quả và thêm vào danh sách wordsList
            while (resultSet.next()) {
                wordsList.add(resultSet.getString("word"));
            }

            // Đảm bảo rằng chúng ta có đúng 5 từ
            if (wordsList.size() < 5) {
                System.out.println("Không lấy đủ 5 từ từ cơ sở dữ liệu.");
                return null;
            }

            // Tạo chuỗi kết quả cách nhau bởi dấu chấm phẩy
            String result = String.join(";", wordsList) + ";";

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu có lỗi xảy ra
    }

    
    
    public static void main(String[] args) {
        String question = renQuestion();
        System.out.println(question);  // In ra chuỗi các từ
    }
}

