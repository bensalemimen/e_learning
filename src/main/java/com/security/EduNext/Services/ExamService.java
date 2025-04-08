package com.security.EduNext.Services;





import com.security.EduNext.Entities.Certificate;
import com.security.EduNext.Entities.Exam;
import com.security.EduNext.Entities.Question;
import com.security.EduNext.Entities.User;
import com.security.EduNext.Repositories.*;
import com.security.EduNext.dto.ExamSubmissionDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.geom.PageSize;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamService {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private EmailService emailService;
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Optional<Exam> getExamById(int id) {
        return examRepository.findById(id);
    }

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }


    public Exam updateExam(int id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));

        exam.setExamTitle(examDetails.getExamTitle());
        exam.setExamDescription(examDetails.getExamDescription());
        exam.setExamDuration(examDetails.getExamDuration());
        exam.setTotalMarks(examDetails.getTotalMarks());
        exam.setPassingScore(examDetails.getPassingScore());
        exam.setScheduledDate(examDetails.getScheduledDate());
        exam.setQuestions(examDetails.getQuestions());

        return examRepository.save(exam);
    }


    public void deleteExam(int id) {
        examRepository.deleteById(id);
    }



    public Exam addQuestionToExam(int idExam, Question question) {
        Exam exam = examRepository.findById(idExam).orElseThrow(() -> new RuntimeException("Exam not found"));
        question.setExam(exam);
        exam.getQuestions().add(question);
        return examRepository.save(exam);
    }

    public Exam getExamWithQuestions(int idExam) {
        return examRepository.findById(idExam).orElseThrow(() -> new RuntimeException("Exam not found"));
    }
    public Question updateQuestion(Long questionId, Question questionDetails) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(questionDetails.getQuestionText());
        question.setAnswerOptions(questionDetails.getAnswerOptions());

        return questionRepository.save(question);
    }
    public List<Question> getQuestionsForExam(int examId) {
        return questionRepository.findByExam_IdExam(examId);
    }

  //  public int correctExam(int userId, ExamSubmissionDTO submission) {
        // Récupérer l'examen par son ID
    //    Exam exam = examRepository.findById(submission.examId)
      //          .orElseThrow(() -> new RuntimeException("Examen non trouvé"));

        // Vérifier que l'utilisateur est bien associé à cet examen
        //Optional<User> user = userRepository.findById(userId);
        //if (!user.isPresent() || !exam.getUsers().contains(user.get())) {
          //  throw new RuntimeException("L'utilisateur n'est pas associé à cet examen");
     //   }

       // List<Question> questions = questionRepository.findByExam_IdExam(submission.examId);
        //int score = 0;
        //int totalMarksPerQuestion = exam.getTotalMarks() / questions.size();

      //  for (Question question : questions) {
        //    String userAnswer = submission.answers.get(question.getId());
          //  if (question.getAnswerOptions().equalsIgnoreCase(userAnswer)) {
            //    score += totalMarksPerQuestion;
            //}
      //  }

        //return score;
    //}




    public Exam addUserToExam(int examId, int userId) {
        // Find the exam by its ID
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

        // Find the user by their ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Add user to exam's users list
    //    exam.getUsers().add(user);

        // Save the updated exam
        return examRepository.save(exam);
    }
    public int submitExam(ExamSubmissionDTO examSubmissionDTO) {
        // Récupérer l'examen par son ID
        Exam exam = examRepository.findById(examSubmissionDTO.getExam().getIdExam())
                .orElseThrow(() -> new RuntimeException("Examen introuvable"));

        // Charger les questions de l'examen
        exam.getQuestions().size();  // Force le chargement des questions pour éviter le Lazy Loading

        // Vérifier que l'utilisateur est bien associé à cet examen
        Optional<User> userOptional = userRepository.findById(examSubmissionDTO.getUser().getId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        User user = userOptional.get();
        // Vérifier si l'utilisateur a déjà passé cet examen
        Optional<ExamSubmissionDTO> existingSubmission = examSubmissionRepository.findByUserAndExam(user, exam);
        if (existingSubmission.isPresent()) {
            throw new RuntimeException("Tu a déjà passé cet examen");
        }
        // Créer la soumission de l'examen
        ExamSubmissionDTO submission = new ExamSubmissionDTO();
        submission.setUser(user);
        submission.setExam(exam);
        submission.setUserAnswers(examSubmissionDTO.getUserAnswers());
        submission.setSubmissionDate(LocalDateTime.now());

        // Calculer le score
        int score = 0;

        for (Question question : exam.getQuestions()) {
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = examSubmissionDTO.getUserAnswers().get(question.getId());

            if (userAnswer == null) {
                System.out.println("Réponse manquante pour la question ID: " + question.getId());
                continue;
            }

            // Normalisation des réponses
            if (userAnswer.contains(")")) {
                userAnswer = userAnswer.split("\\)")[0].trim(); // Extrait "C" de "C) 32 bits"
            }

            if (correctAnswer != null && correctAnswer.trim().equalsIgnoreCase(userAnswer.trim())) {
                score++;
            }
        }

        // Enregistrer la soumission dans la base de données
        submission.setScore(score);
        examSubmissionRepository.save(submission);

        // Vérifier si le score atteint le passingScore et créer un certificat si nécessaire
        if (score >= exam.getPassingScore()) {
            Certificate certificate = new Certificate();
            certificate.setCertificateTitle(exam.getExamTitle());
            certificate.setUserFullName(user.getFirstname() + " " + user.getLastname());
            certificate.setIssuedDate(LocalDate.now());
            certificate.setUser(user);
            certificate.setExam(exam);

            // Enregistrer le certificat dans la base de données
            certificateRepository.save(certificate);
            System.out.println("Certificat généré pour l'utilisateur " + user.getFirstname() + " " + user.getLastname());
            // Générer le PDF du certificat et l'envoyer par mail
            try {
                File certificatePdf = generateCertificatePdf(certificate);
                emailService.sendCertificateEmail(user.getEmail(), exam.getExamTitle(), certificatePdf);
            } catch (IOException e) {
                System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
            }

        }

        System.out.println("Score final : " + score);
        return score;
    }
    public File generateCertificatePdf(Certificate certificate) throws IOException {
        File pdfFile = File.createTempFile("certificate_" + certificate.getUserFullName(), ".pdf");
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        // Charger l'image de fond
        String backgroundPath = getClass().getResource("/static/images/backg.png").getPath();
        ImageData background = ImageDataFactory.create(backgroundPath);
        Image backgroundImg = new Image(background);

       // Obtenir la taille originale de l'image
        float imageWidth = backgroundImg.getImageWidth();
        float imageHeight = backgroundImg.getImageHeight();

         // Obtenir la taille de la page A4
        float pageWidth = PageSize.A4.getWidth();
        float pageHeight = PageSize.A4.getHeight();

        // Calculer le facteur d'échelle pour adapter l'image à la page en conservant le ratio
        float scaleX = pageWidth / imageWidth;
        float scaleY = pageHeight / imageHeight;
        float scale = Math.max(scaleX, scaleY); // Pour s'assurer que l'image couvre toute la page

        backgroundImg.scaleAbsolute(imageWidth * scale, imageHeight * scale);
        backgroundImg.setFixedPosition(0, 0); // Correction ici : Utiliser setFixedPosition()

       // Ajouter en arrière-plan
        document.add(backgroundImg);

        // Ajouter le logo
        String logoPath = getClass().getResource("/static/images/logooo.png").getPath();
        ImageData logo = ImageDataFactory.create(logoPath);
        Image logoImg = new Image(logo);
        logoImg.setFixedPosition(650, 500);
        logoImg.scaleToFit(100, 100);
        document.add(logoImg);

        // Titre
        Paragraph title = new Paragraph("CERTIFICAT DE RÉUSSITE")
                .setFontSize(30)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title.setFixedPosition(210, 470, 400));

        // Informations utilisateur
        document.add(new Paragraph("Titre de l'examen: " + certificate.getCertificateTitle())
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(210, 400, 400));
        document.add(new Paragraph("Nom de l'utilisateur: " + certificate.getUserFullName())
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(210, 350, 400));
        document.add(new Paragraph("Date d'émission: " + certificate.getIssuedDate())
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(210, 300, 400));
        // Ajouter la signature
        String signPath = getClass().getResource("/static/images/signn.png").getPath();
        ImageData sign = ImageDataFactory.create(signPath);
        Image signImg = new Image(sign);
        signImg.setFixedPosition(550, 150);
        signImg.scaleToFit(200, 100);
        document.add(signImg);

        document.close();
        return pdfFile;
    }







    public List<Exam> searchExamsByTitle(String title) {
        return examRepository.findByExamTitleContainingIgnoreCase(title);
    }


    public List<ExamSubmissionDTO> getTop10ExamSubmissions() {
        return examSubmissionRepository.findTop10ByOrderByScoreDesc();
    }
    public List<ExamSubmissionDTO> getExamSubmissionsByUser(Long userId) {
        // Récupère les résultats de l'utilisateur par ID
        return examSubmissionRepository.findByUserId(userId);
    }
    // Récupérer la soumission d'examen d'un utilisateur pour un examen spécifique
    public ExamSubmissionDTO getExamSubmissionByUserAndExam(Long userId, int examId) {
        return examSubmissionRepository.findByUserIdAndExam_IdExam(userId, examId);
    }

}

