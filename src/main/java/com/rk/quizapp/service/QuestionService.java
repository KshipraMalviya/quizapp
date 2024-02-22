package com.rk.quizapp.service;

import com.rk.quizapp.model.Question;
import com.rk.quizapp.dao.QuestionDao;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        logger.info("Attempting to retrieve all questions from the database...");
        List<Question> questions = questionDao.findAll();
        logger.info("Retrieved {} questions from the database", questions.size());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("Question created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error in request!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            questionDao.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            throw new EntityNotFoundException("No question with id {}" + id);
        }
    }

    public ResponseEntity<String> updateQuestion(Question question) {

        Optional<Question> optionalQuestion = questionDao.findById(question.getId());
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = optionalQuestion.get();

            existingQuestion.setQuestionTitle(question.getQuestionTitle());
            existingQuestion.setCategory(question.getCategory());
            existingQuestion.setDifficultylevel(question.getDifficultylevel());
            existingQuestion.setOption1(question.getOption1());
            existingQuestion.setOption2(question.getOption2());
            existingQuestion.setOption3(question.getOption3());
            existingQuestion.setOption4(question.getOption4());

            questionDao.save(existingQuestion);
        } else {
            throw new EntityNotFoundException("Question not found with id: " + question.getId());
        }
        return new ResponseEntity<>("Updated question successfully!", HttpStatus.OK);
    }
}
