package com.my;

import com.my.pojo.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class SpringbootSqlApplicationTests {

    @Test
    public void testInsert(@Autowired JdbcTemplate jdbcTemplate) {
        String sql = "insert into tbl_book values(2, '小说', '三国演义', '吴冠中')";
        jdbcTemplate.update(sql);
    }

    @Test
    public void testQuery(@Autowired JdbcTemplate jdbcTemplate) {
        String sql = "select * from tbl_book";
        RowMapper<Book> rowMapper = new RowMapper<>() {
            @Override
            public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setType(rs.getString("type"));
                book.setName(rs.getString("name"));
                book.setDescription(rs.getString("description"));
                return book;
            }
        };
        List<Book> list = jdbcTemplate.query(sql, rowMapper);
        list.forEach(System.out::println);
    }

}
