package com.security.EduNext.Controllers;


import com.security.EduNext.Entities.Exam;
import com.security.EduNext.Entities.Question;
import com.security.EduNext.Repositories.ExamSubmissionRepository;
import com.security.EduNext.Repositories.QuestionRepository;
import com.security.EduNext.Services.ExamService;
import com.security.EduNext.dto.ExamSubmissionDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://localhost:4200")
public class ExamController {
    @Autowired
    ExamService examService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ExamSubmissionRepository examSubmissionRepository;


   //http://localhost:8093/api/exams
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();

        // Vérifie si des examens sont trouvés et renvoie une réponse appropriée
        if (exams.isEmpty()) {
            return ResponseEntity.noContent().build(); // Code 204 No Content si aucun examen n'est trouvé
        }

        // Sinon, renvoie une réponse avec un code 200 OK
        return ResponseEntity.ok(exams);
    }
   //http://localhost:8093/api/exams/1
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable int id) {
        return examService.getExamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
  //http://localhost:8093/api/exams
    @PostMapping
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }
    //http://localhost:8093/api/exams/23
    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable int id, @RequestBody Exam examDetails) {
        Exam updatedExam = examService.updateExam(id, examDetails);
        return ResponseEntity.ok(updatedExam);
    }
    //http://localhost:8093/api/exams/19
    @DeleteMapping("/{idExam}")
    public ResponseEntity<Void> deleteExam(@PathVariable int idExam) {
        examService.deleteExam(idExam);
        return ResponseEntity.noContent().build();
    }
    //http://localhost:8093/api/exams/a/19/questions
    //{
    //  "questionText": "What is the capital of France?",
    //  "answerOptions": " Paris | Rome | Berlin | Madrid ",
    //  "correctAnswer": "Paris"
    //}

    @PostMapping("/a/{idExam}/questions")
    public ResponseEntity<Exam> addQuestionToExam(@PathVariable int idExam, @RequestBody Question question) {
        Exam updatedExam = examService.addQuestionToExam(idExam, question);
        return ResponseEntity.ok(updatedExam);
    }

    //http://localhost:8093/api/exams/ex/19
    @GetMapping("/ex/{idExam}")
    public ResponseEntity<Exam> getExamWithQuestions(@PathVariable int idExam) {
        Exam exam = examService.getExamWithQuestions(idExam);
        return ResponseEntity.ok(exam);
    }
    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
        // Assurez-vous que l'examen associé est bien chargé et lié à la question
        Optional<Question> existingQuestionOpt = questionRepository.findById(id);
        if (existingQuestionOpt.isPresent()) {
            Question existingQuestion = existingQuestionOpt.get();
            // Mettre à jour les champs de la question (exemple)
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setAnswerOptions(updatedQuestion.getAnswerOptions());
            // Garder la relation avec l'examen inchangée (si c'est nécessaire)
            existingQuestion.setExam(existingQuestion.getExam());
            questionRepository.save(existingQuestion);
            return ResponseEntity.ok(existingQuestion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //http://localhost:8093/api/exams/19/questions
    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable int examId) {
        return ResponseEntity.ok(examService.getQuestionsForExam(examId));
    }
    /*   @PostMapping("/subm")
       public ResponseEntity<Integer> submitExam(@RequestBody ExamSubmissionDTO submission) {
           int score = examService.correctExam(submission);
           return ResponseEntity.ok(score);
       }*/
   //http://localhost:8093/api/exams/submit
    @PostMapping("/submit")
    public ResponseEntity<?> submitExam(@RequestBody ExamSubmissionDTO examSubmissionDTO) {
        try {
            // Debugging pour vérifier que l'examen est bien passé
            System.out.println("Exam  dans DTO: " + examSubmissionDTO.getExam().getIdExam());

            int score = examService.submitExam(examSubmissionDTO);
            return ResponseEntity.ok("" + score);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la soumission de l'examen: " + e.getMessage());
        }
    }
    @GetMapping("/user/{userId}")
    public List<ExamSubmissionDTO> getResultsByUser(@PathVariable Long userId) {
        return examSubmissionRepository.findByUserId(userId);
    }

    //http://localhost:8093/api/exams/1/addUser/18
    @PostMapping("/{examId}/addUser/{userId}")
    public ResponseEntity<Exam> addUserToExam(@PathVariable int examId, @PathVariable int userId) {
        Exam updatedExam = examService.addUserToExam(examId, userId);
        return ResponseEntity.ok(updatedExam);
    }


    @GetMapping("/search")
    public List<Exam> searchExams(@RequestParam String title) {
        return examService.searchExamsByTitle(title);
    }

   //http://localhost:8093/api/exams/top10
    @GetMapping("/top10")
    public List<ExamSubmissionDTO> getTop10ExamSubmissions() {
        return examService.getTop10ExamSubmissions();
    }

    // Récupérer la liste des examens passés par un utilisateur
    @GetMapping("/user/{userId}/list")
    public List<ExamSubmissionDTO> getPassedExams(@PathVariable Long userId) {
        return examService.getExamSubmissionsByUser(userId);
    }

    // Récupérer les résultats détaillés d'un examen spécifique pour un utilisateur
    @GetMapping("/user/{userId}/result/{examId}")
    public ExamSubmissionDTO getExamResult(@PathVariable Long userId, @PathVariable int examId) {
        return examService.getExamSubmissionByUserAndExam(userId, examId);
    }



}




