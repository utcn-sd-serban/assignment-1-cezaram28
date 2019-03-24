package com.cezaram28.Assignment1.controller;

import com.cezaram28.Assignment1.entity.*;
import com.cezaram28.Assignment1.exception.AnswerNotFoundException;
import com.cezaram28.Assignment1.exception.QuestionNotFoundException;
import com.cezaram28.Assignment1.exception.UserNotFoundException;
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
            try {
                done = handleCommand(command);
            } catch(RuntimeException e){
                e.printStackTrace();
            }
//            } catch (UserNotFoundException userNotFoundException) {
//                print("The user was not found");
//            } catch (QuestionNotFoundException questionNotFoundException) {
//                print("The question was not found");
//            } catch (AnswerNotFoundException answerNotFoundException) {
//                print("The answer was not found");
//            }
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
        Optional<User> user = userManagementService.findByCredentials(username,password);
        if(user.isPresent()){
            if(user.get().getIsBanned())
                print("You are banned indefinitely");
            else
                this.user = user.get();
        } else print("Bad credentials!");
    }

    private void logout(){
        user = null;
        print("Logged out");
    }

    private void handleListUsers() {
        if(user==null) print("Please log in");
        else
        userManagementService.listUsers().forEach(u->print(u.toString()));
    }

    private void handleListQuestions() {
        if(user==null) print("Please log in");
        else {
            List<Question> questions = questionManagementService.listQuestions();
            questions.sort((q1,q2)->q1.getCreationDate().after(q2.getCreationDate())?-1:1);
            questions.forEach(question -> print(question.toString()));
        }
    }

    private void handleListQuestionId(){
        if(user==null) print("Please log in");
        else {
            print("Question id:");
            int id = scanner.nextInt();

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){

                print(question.get().toString());
                List<Answer> answers = answerManagementService.listAllToQuestion(id).get();
                if(answers.isEmpty()){
                    print("No answers");
                } else {
                    answers.sort((a1, a2) -> a1.getVoteCount() >= a2.getVoteCount() ? -1 :1 );
                    answers.forEach(answer ->  print(answer.toString()));
                }

            } else {
                print("Question not found!");
            }
        }
    }

    private void handleListQuestionsUser() {
        if (user == null) print("Please log in");
        else {
            int userId = scanner.nextInt();
            List<Question> questions = questionManagementService.listAllByUser(userId).get();
            questions.sort((q1,q2)->q1.getCreationDate().after(q2.getCreationDate())?-1:1);
            questions.forEach(question -> print(question.toString()));
        }
    }

    private void handleListAnswers() {
        if(user==null) print("Please log in");
        else{
            List<Answer> answers = answerManagementService.listAnswers();
            answers.sort((a1, a2) -> a1.getVoteCount() >= a2.getVoteCount() ? -1 : 1);
            answers.forEach(answer -> print(answer.toString()));
        }
    }

    private void handleListAnswersUser() {
        if(user==null) print("Please log in");
        else{
        int userId = scanner.nextInt();
            List<Answer> answers = answerManagementService.listAllByUser(userId).get();
            answers.sort((a1, a2) -> a1.getVoteCount() >= a2.getVoteCount() ? -1 : 1);
            answers.forEach(answer -> print(answer.toString()));
        }
    }

    private void handleListAnswersQuestion() {
        if(user==null) print("Please log in");
        else{
            print("Question Id");
            int questionId = scanner.nextInt();
            List<Answer> answers = answerManagementService.listAllToQuestion(questionId).get();
            answers.sort((a1, a2) -> a1.getVoteCount() >= a2.getVoteCount() ? -1 : 1);
            answers.forEach(answer -> print(answer.toString()));
        }
    }

    private void handleAddUser() {
        print("Username:");
        String username = scanner.next().trim();
        print("Email:");
        String email = scanner.next().trim();
        print("Password:");
        String password = scanner.next().trim();
        List<User> users = userManagementService.listUsers();
        for(User u : users) {
            if(u.getUsername().equals(username) || u.getEmail().equals(email)){
                print("User already exists");
                return;
            }
        }
        User user = userManagementService.addUser(username, password, email);
        print("Created user: " + user + ".");
    }

    private void handleAddQuestion() {
        if (user == null) print("Please log in");
        else {
            print("Title:");
            scanner.nextLine();
            String title = scanner.nextLine().trim();
            print("Text:");
            String text = scanner.nextLine().trim();
            print("Tags:");
            String[] t = scanner.nextLine().split(", ");

            ArrayList<Tag> tags = new ArrayList<Tag>();
            for (int i = 0; i < t.length; i++)
                tags.add(new Tag(null, t[i]));

            tags.forEach(tag -> tagManagementService.addTag(tag));
            Question question = questionManagementService.addQuestion(new Question(title, this.user, text, tags));
            print("Created question: " + question + ".");
        }
    }

    private void handleAddAnswer() {
        if (user == null) print("Please log in");
        else {
            print("Question id:");
            int id = scanner.nextInt();
            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                print("Text:");
                scanner.nextLine();
                String text = scanner.nextLine().trim();
                Answer answer = answerManagementService.addAnswer(new Answer(question.get(),this.user,text));
                print("Created answer: " + answer + ".");
            } else {
                print("Question not found");
            }
        }
    }

    private void handleRemoveUser() {
        if (user == null) print("Please log in");
        else {
            print("User id:");
            int id = scanner.nextInt();
            Optional<User> user = userManagementService.findById(id);
            if(user.isPresent()){
                userManagementService.removeUser(id);
                print("User removed");
            }
            else {
                print("User not found");
            }
        }
    }

    private void handleRemoveQuestion() {
        if (user == null) print("Please log in");
        else {
            print("Question id:");
            int id = scanner.nextInt();
            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                if(user.getIsAdmin()) {
                    questionManagementService.removeQuestion(id);
                    print("Question removed");
                } else {
                    print("You do not have the right to do this");
                }
            }
            else {
                print("Question not found");
            }
        }
    }

    private void handleRemoveAnswer() {
        if (user == null) print("Please log in");
        else {
            print("Answer id:");
            int id = scanner.nextInt();
            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getAuthor().equals(user) || user.getIsAdmin() == true){
                    answerManagementService.removeAnswer(id);
                    print("Answer removed");
                } else {
                    print("Not your answer");
                }
            }
            else {
                print("Answer not found");
            }
        }
    }

    private void handleEditQuestion() {
        if (user == null) print("Please log in");
        else {
            print("Question id:");
            int id = scanner.nextInt();
            //scanner.nextLine();
            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                if(user.getIsAdmin()){
                    print("New title:");
                    String title = scanner.nextLine().trim();
                    print("New text:");
                    String text = scanner.nextLine().trim();
                    question.get().setTitle(title);
                    question.get().setText(text);
                    questionManagementService.addQuestion(question.get());
                    print("Question edited");
                } else {
                    print("You do not have the right to do this");
                }
            }
            else {
                print("Question not found");
            }
        }
    }

    private void handleEditAnswer() {
        if (user == null) print("Please log in");
        else {
            print("Answer id:");
            int id = scanner.nextInt();
            //scanner.nextLine();
            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getAuthor().equals(user) || user.getIsAdmin()){
                    print("New text:");
                    String text = scanner.nextLine().trim();
                    answer.get().setText(text);
                    answerManagementService.addAnswer(answer.get());
                    print("Answer edited");
                } else {
                    print("Not your answer");
                }
            }
            else {
                print("Answer not found");
            }
        }
    }

    private void handleSearchTitle(){
        if (user == null) print("Please log in");
        else {
            print("Title:");
            scanner.nextLine();
            String text = scanner.nextLine().trim();
            final String splitText = text.split("\n")[0];

            List<Question> questions = questionManagementService.listQuestions().stream().filter(question -> question.getTitle().contains(splitText)).collect(Collectors.toList());
            questions.sort((q1,q2)->q1.getCreationDate().after(q2.getCreationDate())?-1:1);

            if(questions.isEmpty()){
                print("No results found");
            } else {
                questions.forEach(question -> print(question.toString()));
            }
        }
    }

    private void handleSearchTag(){
        if (user == null) print("Please log in");
        else {
            print("Tag:");
            scanner.nextLine();
            String text = scanner.nextLine().trim();
            text = text.split("\n")[0];

            List<Question> questions = new ArrayList<>();
            for(Question q : questionManagementService.listQuestions()){
                for(Tag t : q.getTags()){
                    if(t.getName().equals(text)){
                        questions.add(q);
                    }
                }
            }
            questions.sort((q1,q2)->q1.getCreationDate().after(q2.getCreationDate())?-1:1);

            if(questions.isEmpty()){
                print("No results found");
            } else {
                questions.forEach(question -> print(question.toString()));
            }
        }
    }

    private void handleUpvoteQuestion() {
        if(user == null) {
            print("Please log in");
        } else {
            print("Question id: ");
            int id = scanner.nextInt();

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                if(question.get().getAuthor().getId() == user.getId()){
                    print("You can't vote your own question");
                } else {
                    Vote vote = new Vote(null, "up", question.get(), null, user);
                    Optional<Vote> v = voteManagementService.findByQuestion(question.get().getId(), user.getId());
                    if(v.isPresent()){
                        if(v.get().getType().equals(vote.getType())){
                            print("Already upvoted");
                        } else {
                            // compute score by modifying parameters +7 question author, +2 question
                            v.get().setType("up");
                            voteManagementService.save(v.get());

                            question.get().setVoteCount(question.get().getVoteCount() + 2);
                            questionManagementService.addQuestion(question.get());

                            question.get().getAuthor().setScore(question.get().getAuthor().getScore() + 7);
                            userManagementService.addUser(question.get().getAuthor());
                        }
                    } else {
                        vote.setType("up");
                        voteManagementService.save(vote);

                        question.get().setVoteCount(question.get().getVoteCount() + 1);
                        questionManagementService.addQuestion(question.get());

                        question.get().getAuthor().setScore(question.get().getAuthor().getScore() + 5);
                        userManagementService.addUser(question.get().getAuthor());
                    }

                    voteManagementService.save(vote);

                    print("Upvoted");
                }
            } else {
                print("Question not found");
            }
        }
    }

    private void handleDownvoteQuestion() {
        if(user == null) {
            print("Please log in");
        } else {
            print("Question id: ");
            int id = scanner.nextInt();

            Optional<Question> question = questionManagementService.findById(id);
            if(question.isPresent()){
                if(question.get().getAuthor().getId() == user.getId()){
                    print("You can't vote your own question");
                } else {
                    Vote vote = new Vote(null, "down", question.get(), null, user);
                    Optional<Vote> v = voteManagementService.findByQuestion(question.get().getId(), user.getId());
                    if(v.isPresent()){
                        if(v.get().getType().equals(vote.getType())){
                            print("Already downvoted");
                        } else {
                            // compute score by modifying parameters -7 question author, -2 question
                            v.get().setType("down");
                            voteManagementService.save(v.get());

                            question.get().setVoteCount(question.get().getVoteCount() - 2);
                            questionManagementService.addQuestion(question.get());

                            question.get().getAuthor().setScore(question.get().getAuthor().getScore() - 7);
                            userManagementService.addUser(question.get().getAuthor());
                        }
                    } else {
                        vote.setType("down");
                        voteManagementService.save(vote);

                        question.get().setVoteCount(question.get().getVoteCount() - 1);
                        questionManagementService.addQuestion(question.get());

                        question.get().getAuthor().setScore(question.get().getAuthor().getScore() - 2);
                        userManagementService.addUser(question.get().getAuthor());
                    }

                    voteManagementService.save(vote);

                    print("Downvoted");
                }
            } else {
                print("Question not found");
            }
        }
    }

    private void handleUpvoteAnswer() {
        if(user == null) {
            print("Please log in");
        } else {
            print("Answer id: ");
            int id = scanner.nextInt();

            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getAuthor().getId() == user.getId()){
                    print("You can't vote your own answer");
                } else {
                    Vote vote = new Vote(null, "up", null, answer.get(), user);
                    Optional<Vote> v = voteManagementService.findByAnswer(answer.get().getId(), user.getId());
                    if(v.isPresent()){
                        if(v.get().getType().equals(vote.getType())){
                            print("Already upvoted");
                        } else {
                            // compute score by modifying parameters +12 answer author, +1 user, +2 answer
                            v.get().setType("up");
                            voteManagementService.save(v.get());

                            answer.get().setVoteCount(answer.get().getVoteCount() + 2);
                            answerManagementService.addAnswer(answer.get());

                            answer.get().getAuthor().setScore(answer.get().getAuthor().getScore() + 12);
                            userManagementService.addUser(answer.get().getAuthor());

                            user.setScore(user.getScore() + 1);
                            userManagementService.addUser(user);
                        }
                    } else {
                        vote.setType("up");
                        voteManagementService.save(vote);

                        answer.get().setVoteCount(answer.get().getVoteCount() + 1);
                        answerManagementService.addAnswer(answer.get());

                        answer.get().getAuthor().setScore(answer.get().getAuthor().getScore() + 10);
                        userManagementService.addUser(answer.get().getAuthor());
                    }

                    voteManagementService.save(vote);

                    print("Upvoted");
                }
            } else {
                print("Answer not found");
            }
        }
    }

    private void handleDownvoteAnswer() {
        if(user == null) {
            print("Please log in");
        } else {
            print("Answer id: ");
            int id = scanner.nextInt();

            Optional<Answer> answer = answerManagementService.findById(id);
            if(answer.isPresent()){
                if(answer.get().getAuthor().getId() == user.getId()){
                    print("You can't vote your own answer");
                } else {
                    Vote vote = new Vote(null, "up", null, answer.get(), user);
                    Optional<Vote> v = voteManagementService.findByAnswer(answer.get().getId(), user.getId());
                    if (v.isPresent()) {
                        if (v.get().getType().equals(vote.getType())) {
                            print("Already downvoted");
                        } else {
                            // compute score by modifying parameters -12 answer author, -1 user, -2 answer
                            v.get().setType("down");
                            voteManagementService.save(v.get());

                            answer.get().setVoteCount(answer.get().getVoteCount() - 2);
                            answerManagementService.addAnswer(answer.get());

                            answer.get().getAuthor().setScore(answer.get().getAuthor().getScore() - 12);
                            userManagementService.addUser(answer.get().getAuthor());

                            user.setScore(user.getScore() - 1);
                            userManagementService.addUser(user);
                        }
                    } else {
                        vote.setType("down");
                        voteManagementService.save(vote);

                        answer.get().setVoteCount(answer.get().getVoteCount() - 1);
                        answerManagementService.addAnswer(answer.get());

                        answer.get().getAuthor().setScore(answer.get().getAuthor().getScore() - 2);
                        userManagementService.addUser(answer.get().getAuthor());

                        user.setScore(user.getScore() - 1);
                        userManagementService.addUser(user);

                        voteManagementService.save(vote);

                        print("Downvoted");
                    }
                }
            } else {
                print("Answer not found");
            }
        }
    }

    private void makeAdmin() {
        if(user.getIsAdmin()) {
            print("User id:");
            int id = scanner.nextInt();
            Optional<User> u = userManagementService.findById(id);
            if(u.isPresent()) {
                u.get().setIsAdmin(true);
                userManagementService.addUser(u.get());
            } else {
                print("User not found");
            }
        }
    }

    private void ban() {
        if(user.getIsAdmin()) {
            print("User id:");
            int id = scanner.nextInt();
            Optional<User> u = userManagementService.findById(id);
            if(u.isPresent()) {
                u.get().setIsBanned(true);
                userManagementService.addUser(u.get());
            } else {
                print("User not found");
            }
        }
    }

    private void print(String value) {
        System.out.println(value);
    }
}
