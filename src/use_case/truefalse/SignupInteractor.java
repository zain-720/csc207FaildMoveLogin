package use_case.truefalse;

import entity.User;
import entity.UserFactory;

import java.time.LocalDateTime;

// Define the SignupInteractor class, which implements the SignupInputBoundary interface.
public class SignupInteractor implements SignupInputBoundary {
    final SignupUserDataAccessInterface userDataAccessObject;
    final SignupOutputBoundary userPresenter;
    final UserFactory userFactory;

    // Constructor for the SignupInteractor class.
    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    // Implementation of the execute method from the SignupInputBoundary interface.
    @Override
    public void execute(SignupInputData signupInputData) {
        // Check if a user with the provided username already exists.
        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            userPresenter.failView("User already exists.");
        }
        // Check if the provided password and repeat password do not match.
        else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.failView("Passwords don't match.");
        }
        // If both conditions are satisfied, create a new user and save it.
        else {
            LocalDateTime now = LocalDateTime.now();
            User user = userFactory.create(signupInputData.getUsername(), signupInputData.getPassword(), now);
            userDataAccessObject.save(user);

            // Create a SignupOutputData object and notify the presenter of success.
            SignupOutputData signupOutputData = new SignupOutputData(user.getName(), now.toString(), false);
            userPresenter.successView(signupOutputData);
        }
    }
}
