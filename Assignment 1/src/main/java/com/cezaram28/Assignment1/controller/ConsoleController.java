package com.cezaram28.Assignment1.controller;

import com.cezaram28.Assignment1.entity.*;
import com.cezaram28.Assignment1.exception.*;
import com.cezaram28.Assignment1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsoleController implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final UserManagementService userManagementService;
    private final QuestionManagementService questionManagementService;
    private final AnswerManagementService answerManagementService;
    private final TagManagementService tagManagementService;
    private final VoteManagementService voteManagementService;
    private User user = null;

    @Override
    public void run(String... args) {
        print("Welcome to Stack Overflow.");
        boolean done = false;
        while (!done) {
            print("Enter a command: ");
            String command = scanner.next().trim();
            if(user==null && ! command.equals("login") && ! command.equals("addUser") && ! command.equals("exit")) {
                print("Please log in");
                done = false;
            } else {

                try {
                    done = handleCommand(command);
                } catch(AnswerNotFoundException e) {
                    print("No answer found!");
                } catch(QuestionNotFoundException e) {
                    print("No question found!");
                } catch(TagNotFoundException e) {
                    print("No tag found!");
                } catch(UserNotFoundException e) {
                    print("No user found!");
                } catch(BannedUserException e) {
                    print("User banned indefinitely!");
                } catch(NoAdminException e) {
                    print("You do not have the right to do this!");
                } catch(BadCredentialsException e) {
                    print("Wrong username/password!");
                } catch(UserExistsException e) {
                    print("User already exists!");
                } catch(VoteNotFoundException e) {
                    print("No vote found!");
                } catch(YourPostException e) {
                    print("You can't vote your own posts!");
                } catch(UpvotedException e) {
                    print("Already upvoted this!");
                } catch(DownvotedException e) {
                    print("Already downvoted this!");
                } catch(RuntimeException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean handleCommand(String command) {
        switch (command) {
            case "login":
                login();
                return false;
            case "logout":
                logout();
                return false;
            case "listUsers":
                handleListUsers();
                return false;
            case "addUser":
                handleAddUser();
                return false;
            case "removeUser":
                handleRemoveUser();
                return false;
            case "listQuestions":
                handleListQuestions();
                return false;
            case "listQuestionId":
                handleListQuestionId();
                return false;
            case "listQuestionsByUser":
                handleListQuestionsUser();
                return false;
            case "addQuestion":
                handleAddQuestion();
                return false;
            case "removeQuestion":
                handleRemoveQuestion();
                return false;
            case "editQuestion":
                handleEditQuestion();
                return false;
            case "listAnswers":
                handleListAnswers();
                return false;
            case "listAnswersByQuestion":
                handleListAnswersQuestion();
                return false;
            case "listAnswersByUser":
                handleListAnswersUser();
                return false;
            case "addAnswer":
                handleAddAnswer();
                return false;
            case "removeAnswer":
                handleRemoveAnswer();
                return false;
            case "editAnswer":
                handleEditAnswer();
                return false;
            case "searchTitle":
                handleSearchTitle();
                return false;
            case "searchTag":
                handleSearchTag();
                return false;
            case "upvoteQuestion":
                handleUpvoteQuestion();
                return false;
            case "downvoteQuestion":
                handleDownvoteQuestion();
                return false;
            case "upvoteAnswer":
                handleUpvoteAnswer();
                return false;
            case "downvoteAnswer":
                handleDownvoteAnswer();
                return false;
            case "makeAdmin":
                makeAdmin();
                return false;
            case "ban":
                ban();
                return false;
            case "exit":
                return true;
            default:
                print("Unknown command. Try again.");
                return false;
        }
    }

    private void login() {
        print("Username:");
        String username = scanner.next().trim();
        print("Password:");
        String password = scanner.next().trim();
        user = userManagementService.findByCredentials(username,password);
    }

    private void logout(){
        user = null;
        print("Logged out");
    }

    private void handleListUsers() {
        userManagementService.listUsers().forEach(u->print(u.toString()));
    }

    private void handleListQuestions() {
        List<Question> questions = questionManagementService.listQuestions();
        questions.forEach(question -> print(question.toString()));
    }

    private void handleListQuestionId(){

        print("Question id:");
        int id = scanner.nextInt();
        Question question = questionManagementService.findById(id);
        print(question.toString());
        List<Answer> answers = answerManagementService.listAllToQuestion(id);
        answers.forEach(answer ->  print(answer.toString()));

    }

    private void handleListQuestionsUser() {
        print("User id:");
        int userId = scanner.nextInt();
        List<Question> questions = questionManagementService.listAllByUser(userId);
        questions.forEach(question -> print(question.toString()));
    }

    private void handleListAnswers() {
        List<Answer> answers = answerManagementService.listAnswers();
        answers.forEach(answer -> print(answer.toString()));
    }

    private void handleListAnswersUser() {
        print("User id:");
        int userId = scanner.nextInt();
        List<Answer> answers = answerManagementService.listAllByUser(userId);
        answers.forEach(answer -> print(answer.toString()));
    }

    private void handleListAnswersQuestion() {
        print("Question Id");
        int questionId = scanner.nextInt();
        List<Answer> answers = answerManagementService.listAllToQuestion(questionId);
        answers.forEach(answer -> print(answer.toString()));
    }

    private void handleAddUser() {
        this.user = null;
        print("Username:");
        String username = scanner.next().trim();
        print("Email:");
        String email = scanner.next().trim();
        print("Password:");
        String password = scanner.next().trim();
        User user = userManagementService.addUser(username, password, email);
        print("Created user: " + user + ".");
    }

    private void handleAddQuestion() {
        print("Title:");
        scanner.nextLine();
        String title = scanner.nextLine().trim();
        print("Text:");
        String text = scanner.nextLine().trim();
        print("Tags:");
        ArrayList<Tag> tags = tagManagementService.addTags(scanner.nextLine().split(", "));
        Question question = questionManagementService.addQuestion(new Question(title, this.user, text, tags));
        print("Created question: " + question + ".");
    }

    private void handleAddAnswer() {
        print("Question id:");
        int id = scanner.nextInt();
        Question question = questionManagementService.findById(id);
        print("Text:");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        Answer answer = answerManagementService.addAnswer(new Answer(question,this.user,text));
        print("Created answer: " + answer + ".");
    }

    private void handleRemoveUser() {
        print("User id:");
        int id = scanner.nextInt();
        userManagementService.removeUser(id);
        print("User removed");
    }

    private void handleRemoveQuestion() {
        print("Question id:");
        int id = scanner.nextInt();
        questionManagementService.removeQuestion(id, user);
        print("Question removed");
    }

    private void handleRemoveAnswer() {
        print("Answer id:");
        int id = scanner.nextInt();
        answerManagementService.removeAnswer(id, user);
        print("Answer removed");
    }

    private void handleEditQuestion() {
        print("Question id:");
        int id = scanner.nextInt();
        Question question = questionManagementService.findById(id);
        print("New title:");
        scanner.nextLine();
        String title = scanner.nextLine().trim();
        print("New text:");
        String text = scanner.nextLine().trim();
        question.setTitle(title);
        question.setText(text);
        questionManagementService.editQuestion(question, user);
        print("Question edited");
    }

    private void handleEditAnswer() {
        print("Answer id:");
        int id = scanner.nextInt();
        Answer answer = answerManagementService.findById(id);
        print("New text:");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        answer.setText(text);
        answerManagementService.editAnswer(answer, user);
        print("Answer edited");
    }

    private void handleSearchTitle(){

        print("Title:");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        List<Question> questions = questionManagementService.getByTitle(text.split("\n")[0]);
        questions.forEach(question -> print(question.toString()));
    }

    private void handleSearchTag(){
        print("Tag:");
        scanner.nextLine();
        String text = scanner.nextLine().trim();
        List<Question> questions = questionManagementService.getByTag(text.split("\n")[0]);
        questions.forEach(question -> print(question.toString()));
    }

    private void handleUpvoteQuestion() {
        print("Question id: ");
        int id = scanner.nextInt();
        Question question = questionManagementService.findById(id);
        voteManagementService.upvoteQuestion(question, user);
        print("Upvoted");

    }

    private void handleDownvoteQuestion() {
        print("Question id: ");
        int id = scanner.nextInt();
        Question question = questionManagementService.findById(id);
        voteManagementService.downvoteQuestion(question, user);
        print("Downvoted");
    }

    private void handleUpvoteAnswer() {
        print("Answer id: ");
        int id = scanner.nextInt();
        Answer answer = answerManagementService.findById(id);
        voteManagementService.upvoteAnswer(answer, user);
        print("Upvoted");
    }

    private void handleDownvoteAnswer() {
        print("Answer id: ");
        int id = scanner.nextInt();
        Answer answer = answerManagementService.findById(id);
        voteManagementService.downvoteAnswer(answer, user);
        print("Downvoted");
    }

    private void makeAdmin() {
        print("User id:");
        int id = scanner.nextInt();
        User u = userManagementService.ban(id,user);
        print("User made admin");
    }

    private void ban() {
        print("User id:");
        int id = scanner.nextInt();
        User u = userManagementService.ban(id,user);
        print("User banned");
    }

    private void print(String value) {
        System.out.println(value);
    }
}
