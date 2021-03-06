# TaskMaster

the app have a three pages.

## 1- Home page

![HomePage](./img/TaskMasterHome.png)

## 2- Add Task page

![AddTask](./img/TaskMasterAddTask.png)

## 3- All Task page

![AllTasks](./img/TaskMasterAllTasks.png)

## 4- Setting page

![setteng](./img/TaskMasterSetting.png)

## 5- Task Details page

![Task Details](./img/TaskMasterTaskDetails.png)

## 6- Recycler View

![RecyclerView](./img/TaskMasterTaskRecyclerView01.png)

## 7- Room

Add Task Form: Modify your Add Task form to save the data entered in as a Task in your local database.

![ROOM](./img/TaskMasterAddTaskDatabase.png)

## 8- Amplify and DynamoDB

Modify your Add Task form to save the data entered in as a Task to DynamoDB.

![AmplifyAndDynamoDB](./img/TaskMasterAmplifyAndDynmoDB.png)

## 9- Related Data

Create a second entity for a team, which has a name and a list of tasks. Update your tasks to be owned by a team.

Manually create three teams by running a mutation exactly three times in your code. (You do NOT need to allow the user to create new teams.)

![RelatedData](./img/Screenshot_1.png)

## Publishing to the Play Store

![Poblishing](./img/TaskMasterAmplifyAndDynmoDB.png)

Publishing is the general process that makes your Android applications available to users. When you publish an Android application you perform two main tasks: You prepare the application for release. During the preparation step you build a release version of your application, which users can download and install on their Android-powered devices.

You release the application to users. During the release step you publicize, sell, and distribute the release version of your application to users.

Preparing your app for release Preparing your application for release is a multi-step process that involves the following tasks:

Configuring your application for release. Building and signing a release version of your application. Testing the release version of your application. Updating application resources for release. Preparing remote servers and services that your application depends on. Releasing your app to users You can release your Android applications several ways. Usually, you release applications through an application marketplace such as Google Play, but you can also release applications on your own website or by sending an application directly to a user.

## Cognito

Add Cognito to your Amplify setup. Add in user login and sign up flows to your application, using Cognito???s pre-built UI as appropriate. Display the logged-in user???s username (or nickname) somewhere relevant in your app.

![login](./img/TaskMasterLogin.png)
![logut](./img/TaskMasterLogout.png)
![signup](./img/TaskMasterSignup.png)

## S3

![S3](./img/TaskMasterS3.png)

## Notifications

Add an intent filter to your application such that a user can hit the ???share??? button on an image in another application, choose TaskMaster as the app to share that image with, and be taken directly to the Add a Task activity with that image pre-selected.

![Notifications](./img/TaskMasterTaskIntentFilter.png)

## Location

When the user adds a task, their location should be retrieved and included as part of the saved Task.

![Location](./img/TaskMasterLocation.png)

On the Task Detail activity, the location of a Task should be displayed if it exists.

![Map](./img/TaskMasterMap.png)


## Intent Filters

### Analytics

On the ???Main??? activity (and any other activities you like), start recording at least one AnalyticsEvent. Make sure you can view instances of that event, including their custom properties, in Amazon Pinpoint.

![Analytics](./img/TaskMasterAmplifyAnalytics.png)

### Text To Speech Second Predictions Integration

On the Task Detail activity, add a button to read out the task???s description using the Amplify Predictions library.

![translate](./img/TaskMasterTranslateAndPlayAudio.png)

## Monetization And AdMob Ads

Banner Ad
On the ???Main??? activity, add a banner ad to the bottom of the page and display a Google test ad there.

Interstitial Ad
Add a button to the ???Main??? activity that allows users to see an interstitial ad. (In a real app, you???ll want to show this during natural transitions/pauses in your app, but we don???t really have that in this application.)

Rewarded Ad
Add a button to the ???Main??? activity that allows users to see a rewarded ad using a Google test ad. When the user clicks the close button, the user should see their reward in a text field next to the button.

![AdMob](./img/TaskMasterTaskAddMob.png)
