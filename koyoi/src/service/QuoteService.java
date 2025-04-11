package com.koyoi.main.service;

import com.koyoi.main.mapper.QuoteMapper;
import com.koyoi.main.vo.QuoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.util.List;

@Service
public class QuoteService {

    private final DataSource dataSource;

    @Autowired
    public QuoteService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private QuoteMapper quoteMapper;

    public List<QuoteVO> getRandomQuotes() {
        return quoteMapper.getRandomQuotes();
    }
}
