# StudentPanel.java - Complete Guide

## 📋 Table of Contents
1. [Overview](#overview)
2. [Class Structure & Fields](#class-structure--fields)
3. [Core Methods Explained](#core-methods-explained)
4. [Important Patterns & Concepts](#important-patterns--concepts)
5. [How to Edit/Modify](#how-to-editmodify)

---

## Overview

**StudentPanel** is the main UI component for students in the Seminar Management System. It uses a **CardLayout** pattern to switch between different "screens" (Register, Status, Registration Form) without creating separate windows.

### Key Architecture Pattern: CardLayout
- **CardLayout** allows multiple panels to exist but only one is visible at a time
- Think of it like a deck of cards - you flip between them
- Cards: "Register", "Status", "RegistrationForm"

---

## Class Structure & Fields

### Instance Variables (Lines 16-38)

```java
// Logging
private static final Logger logger = Logger.getLogger(...);

// User & UI Components
private User currentUser;                    // Currently logged-in student
private JPanel studentPanel;                // Main panel container
private CardLayout cardLayout;              // Manages panel switching
private JPanel cardContainer;               // Container holding all card panels
private JButton registerTabButton;          // Tab button for Register screen
private JButton statusTabButton;             // Tab button for Status screen

// State Management (CRITICAL - tracks student's data)
private Session registeredSeminar = null;    // Seminar student registered for
private Session selectedSeminarTemp = null;  // Temporary: seminar selected during registration
private String submissionStatus = null;      // "Submitted", "Under Evaluation", "Completed"
private String awardResult = "Pending";      // Award status

// Form Input Fields
private JTextField researchTitleField;
private JTextArea abstractField;
private JTextField supervisorNameField;
private JComboBox<String> presentationTypeField;  // Dropdown: "", "Oral", "Poster"
private JTextField materialPathField;              // File path (read-only)
private JLabel errorLabel;                         // Shows validation errors

private final JFrame parent;  // Reference to parent window (for logout)
```

**🔑 Key Understanding:**
- `registeredSeminar` = Student's confirmed registration
- `selectedSeminarTemp` = Temporary selection during form filling
- Form fields are created once and reused (not recreated each time)

---

## Core Methods Explained

### 1. Constructor (Lines 40-43)
```java
public StudentPanel(JFrame parent) {
    this.parent = parent;
    initializePanel();
}
```
**Purpose:** Creates the panel and sets up all UI components  
**When called:** Once when StudentPanel is created  
**To modify:** Change initialization logic here

---

### 2. `initializePanel()` (Lines 49-84)
**Purpose:** Sets up the entire UI structure

**Flow:**
1. Creates main `studentPanel` with BorderLayout
2. Creates top header (welcome + logout)
3. Creates CardLayout system with 3 cards:
   - "Register" - List of available seminars
   - "Status" - Student's submission status
   - "RegistrationForm" - Form to fill registration
4. Creates tab buttons to switch between Register/Status
5. Shows "Register" panel by default

**Important Lines:**
- **Line 59-60:** Creates CardLayout system
- **Line 67-69:** Adds panels to card container with names (these names are used to switch!)
- **Line 82:** Shows "Register" panel initially

**To modify:** Change default panel, add new cards, change layout structure

---

### 3. `createTopPanel()` (Lines 86-109)
**Purpose:** Creates header with welcome message and logout button

**Key Lines:**
- **Line 92-94:** Creates welcome text using `currentUser.getName()` and `currentUser.getId()`
- **Line 100-105:** Logout button - calls `parent.showLoginPanel()` if parent is LoginFrame

**To modify:** Change welcome message format, add more header buttons

---

### 4. `createTabPanel()` (Lines 111-150)
**Purpose:** Creates tab navigation buttons (Register/Status tabs)

**Key Pattern:**
- When Register tab clicked → Show "Register" card, disable Register button, enable Status button
- When Status tab clicked → Show "Status" card, refresh data, disable Status button, enable Register button

**Important Lines:**
- **Line 122-130:** Register tab click handler
- **Line 132-144:** Status tab click handler
- **Line 133:** `refreshStatusPanel()` - refreshes data before showing

**To modify:** Add more tabs, change tab behavior, add tab icons

---

### 5. `createRegisterPanel()` (Lines 153-193)
**Purpose:** Creates the panel showing available seminars to register for

**Logic Flow:**
1. **Line 159-164:** If student already registered → Show message, return early
2. **Line 167:** Get available seminars from database
3. **Line 169-174:** If no seminars → Show "No available seminar" message
4. **Line 181-185:** Loop through seminars, create a card for each
5. **Line 187-190:** Wrap in scrollable pane

**Key Lines:**
- **Line 167:** `getAvailableSeminars()` - fetches from DBHelper
- **Line 182:** `createSeminarCard()` - creates individual seminar card

**To modify:** Change seminar display format, add filters, change card layout

---

### 6. `createSeminarCard()` (Lines 195-236)
**Purpose:** Creates a single seminar card with info and Register button

**Structure:**
- Left side: Seminar ID + Details (date, venue, type)
- Right side: Register button

**Key Lines:**
- **Line 209:** Shows seminar ID
- **Line 212:** Shows seminar details (uses `seminar.getDetails()`)
- **Line 221-225:** Register button with click handler → calls `handleRegistration(seminar)`

**To modify:** Change card design, add more info, change button style

---

### 7. `handleRegistration()` (Lines 238-269)
**Purpose:** Handles when student clicks "Register" on a seminar card

**Flow:**
1. Validate seminar is not null
2. Store seminar in `selectedSeminarTemp` (temporary storage)
3. Refresh registration form panel
4. Switch to "RegistrationForm" card

**Key Lines:**
- **Line 252:** `selectedSeminarTemp = seminar` - Stores selected seminar
- **Line 256:** `refreshRegistrationFormPanel()` - Updates form with seminar info
- **Line 258:** `cardLayout.show(cardContainer, "RegistrationForm")` - Switches to form

**To modify:** Add confirmation dialog, add validation, change navigation flow

---

### 8. `createStatusPanel()` (Lines 271-320)
**Purpose:** Shows student's registration status and award result

**Logic:**
- **Line 276-281:** If no registration → Show "No submission"
- **Line 284-290:** Create status card with border
- **Line 293-307:** Display seminar ID, details, status, award result

**Key Data Displayed:**
- Seminar ID (from `registeredSeminar`)
- Seminar details
- Submission status (`submissionStatus`)
- Award result (`awardResult`)

**To modify:** Add more status info, change display format, add action buttons

---

### 9. `createRegistrationFormPanel()` (Lines 359-503)
**Purpose:** Creates the registration form with all input fields

**Structure:**
1. **Top section (364-378):** Title + Go Back button
2. **Seminar details section (380-397):** Shows selected seminar info
3. **Form fields (399-490):** All input fields using GridBagLayout

**Form Fields (GridBagLayout positions):**
- **Row 0:** Error label (spans 2 columns)
- **Row 1:** Research Title (JTextField)
- **Row 2:** Abstract (JTextArea with scroll)
- **Row 3:** Supervisor Name (JTextField)
- **Row 4:** Presentation Type (JComboBox: "", "Oral", "Poster")
- **Row 5:** Attach Material (File chooser button + path display)
- **Row 6:** Submit button (spans 2 columns, centered)

**Key Lines:**
- **Line 400:** `GridBagLayout` - Flexible grid layout system
- **Line 401:** `GridBagConstraints gbc` - Controls positioning
- **Line 450-451:** Presentation type combo box creation
- **Line 467-479:** File chooser for material attachment
- **Line 489:** Submit button → calls `handleSubmitRegistration()`

**GridBagLayout Pattern:**
```java
gbc.gridx = 0;  // Column (0 = left, 1 = right)
gbc.gridy = 1;  // Row (starts at 0)
gbc.gridwidth = 2;  // Span columns
formFieldsPanel.add(component, gbc);
```

**To modify:** Add/remove fields, change field types, change layout, add validation

---

### 10. `handleSubmitRegistration()` (Lines 517-640)
**Purpose:** Validates and saves registration when Submit button clicked

**Flow:**

#### Step 1: Validation (Lines 518-555)
- Reset error display
- Check each field is filled
- For presentation type: Check if selected (not empty string)
- If invalid → Show error, highlight fields, return early

**Key Validation Lines:**
- **Line 526:** Research title empty check
- **Line 531:** Abstract empty check
- **Line 536:** Supervisor name empty check
- **Line 541-545:** Presentation type validation (must not be empty)
- **Line 547:** Material path empty check

#### Step 2: Extract Session ID (Lines 566-603)
- Converts "SEM001" → 1 (integer)
- Handles errors if format is wrong

**Key Lines:**
- **Line 575:** Strips "SEM" prefix: `sessionIdFull.toUpperCase().replace("SEM", "")`
- **Line 580:** Validates numeric: `sessionIdStr.matches("\\d+")`
- **Line 583:** Converts to int: `Integer.parseInt(sessionIdStr)`

#### Step 3: Save to Database (Lines 605-614)
```java
DBHelper.saveStudentRegistration(
    studentId,
    researchTitleField.getText().trim(),
    abstractField.getText().trim(),
    supervisorNameField.getText().trim(),
    presentationType,
    materialPathField.getText().trim(),
    sessionId
);
```

#### Step 4: Update State (Lines 616-619)
- Set `registeredSeminar` = selected seminar
- Clear `selectedSeminarTemp`
- Set `submissionStatus` = "Submitted"

#### Step 5: Show Success & Navigate (Lines 621-631)
- Show success dialog
- Return to Register panel
- Refresh Register panel (will now show "already registered" message)

**To modify:** Add more validation rules, change error messages, add confirmation, modify database save logic

---

### 11. `setUser()` (Lines 656-692)
**Purpose:** Sets the current logged-in user and refreshes all panels

**Flow:**
1. Set `currentUser`
2. Reset all state variables (clear previous user's data)
3. Clear form fields
4. Refresh all panels with new user's data
5. Load user's data from database

**Key Lines:**
- **Line 660-663:** Reset state (registeredSeminar, submissionStatus, etc.)
- **Line 666:** Clear form
- **Line 675-677:** Refresh all panels
- **Line 690:** `loadStudentData()` - Load from database

**When called:** When student logs in or switches users

**To modify:** Add user-specific initialization, change refresh behavior

---

### 12. `loadStudentData()` (Lines 704-726)
**Purpose:** Loads student's registration data from database

**Loads:**
- Registered seminar (`DBHelper.getStudentRegisteredSeminar()`)
- Submission status (`DBHelper.getStudentSubmissionStatus()`)
- Award result (`DBHelper.getStudentAwardResult()`)

**Key Lines:**
- **Line 707:** Load seminar
- **Line 713:** Load status
- **Line 719:** Load award

**To modify:** Add more data loading, change error handling

---

### 13. Refresh Methods

#### `refreshRegisterPanel()` (Lines 322-341)
- Removes old Register panel
- Creates new one
- Shows it

#### `refreshStatusPanel()` (Lines 343-351)
- Removes old Status panel (index 1)
- Creates new one
- Adds it back

#### `refreshRegistrationFormPanel()` (Lines 505-514)
- Removes old form panel (index 2)
- Creates new one
- Adds it back

**Why refresh?** To update UI when data changes (e.g., after registration)

---

### 14. Helper Methods

#### `getAvailableSeminars()` (Lines 353-357)
- Calls `DBHelper.getAvailableSessions(studentId)`
- Returns list of Session objects

#### `setFieldError()` (Lines 642-644)
- Sets red border on invalid field
- Used during validation

#### `resetFieldBorders()` (Lines 646-652)
- Resets all field borders to gray
- Called before validation

#### `clearRegistrationForm()` (Lines 694-702)
- Clears all form fields
- Resets combo box to index 0 (empty)
- Clears error label

---

## Important Patterns & Concepts

### 1. CardLayout Pattern
```java
// Create CardLayout
cardLayout = new CardLayout();
cardContainer = new JPanel(cardLayout);

// Add panels with names
cardContainer.add(panel1, "Register");
cardContainer.add(panel2, "Status");

// Switch panels
cardLayout.show(cardContainer, "Register");  // Show Register panel
cardLayout.show(cardContainer, "Status");   // Show Status panel
```

**Key:** Panel names ("Register", "Status", "RegistrationForm") are used to switch!

---

### 2. State Management
The class uses instance variables to track state:
- `registeredSeminar` - Confirmed registration
- `selectedSeminarTemp` - Temporary selection during form
- `submissionStatus` - Current status
- `awardResult` - Award status

**Important:** These variables control what UI shows!

---

### 3. GridBagLayout Pattern
```java
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(10, 10, 10, 10);  // Padding
gbc.anchor = GridBagConstraints.WEST;     // Alignment

// Position component
gbc.gridx = 0;  // Column
gbc.gridy = 1;  // Row
formFieldsPanel.add(label, gbc);

gbc.gridx = 1;  // Next column
formFieldsPanel.add(textField, gbc);
```

---

### 4. File Chooser Pattern
```java
JFileChooser fileChooser = new JFileChooser();
fileChooser.setFileFilter(filter);  // Filter file types
int result = fileChooser.showOpenDialog(parent);
if (result == JFileChooser.APPROVE_OPTION) {
    File file = fileChooser.getSelectedFile();
    // Use file
}
```

---

### 5. Validation Pattern
```java
boolean isValid = true;
if (field.isEmpty()) {
    setFieldError(field);  // Highlight error
    isValid = false;
}
if (!isValid) {
    errorLabel.setText("Error message");
    return;  // Stop processing
}
// Continue if valid
```

---

## How to Edit/Modify

### Adding a New Form Field

1. **Add field variable** (around line 34):
```java
private JTextField newField;
```

2. **Create field in form** (in `createRegistrationFormPanel()`, around line 454):
```java
// New Field
gbc.gridx = 0;
gbc.gridy = 5;  // Adjust row number
formFieldsPanel.add(new JLabel("New Field:"), gbc);
newField = new JTextField(30);
newField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
gbc.gridx = 1;
formFieldsPanel.add(newField, gbc);
```

3. **Add validation** (in `handleSubmitRegistration()`, around line 545):
```java
if (newField.getText().trim().isEmpty()) {
    setFieldError(newField);
    isValid = false;
}
```

4. **Add to reset methods**:
   - `resetFieldBorders()` - Add border reset
   - `clearRegistrationForm()` - Add `newField.setText("")`

5. **Save to database** (in `handleSubmitRegistration()`, around line 611):
   - Add parameter to `DBHelper.saveStudentRegistration()` call

---

### Changing Panel Layout

**To change Register panel:**
- Edit `createRegisterPanel()` (line 153)
- Modify seminar card layout in `createSeminarCard()` (line 195)

**To change Status panel:**
- Edit `createStatusPanel()` (line 271)

**To change Form layout:**
- Edit `createRegistrationFormPanel()` (line 359)
- Adjust GridBagLayout positions (change `gbc.gridx` and `gbc.gridy`)

---

### Adding a New Tab/Screen

1. **Add card to CardLayout** (in `initializePanel()`, around line 69):
```java
JPanel newPanel = createNewPanel();
cardContainer.add(newPanel, "NewPanel");
```

2. **Create panel method**:
```java
private JPanel createNewPanel() {
    JPanel panel = new JPanel();
    // Add components
    return panel;
}
```

3. **Add tab button** (in `createTabPanel()`, around line 116):
```java
JButton newTabButton = StudentPanelButtons.createTabButton("New Tab");
newTabButton.addActionListener(e -> {
    cardLayout.show(cardContainer, "NewPanel");
    // Update button states
});
tabPanel.add(newTabButton);
```

---

### Modifying Validation Rules

**In `handleSubmitRegistration()` (line 517):**

**Add custom validation:**
```java
// Example: Check research title length
if (researchTitleField.getText().trim().length() < 10) {
    setFieldError(researchTitleField);
    errorMessage = "Research title must be at least 10 characters";
    isValid = false;
}
```

**Add regex validation:**
```java
// Example: Check email format
if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    setFieldError(emailField);
    isValid = false;
}
```

---

### Changing Database Operations

**To modify what's saved:**
- Edit `DBHelper.saveStudentRegistration()` call (line 606)
- Add/remove parameters

**To load different data:**
- Edit `loadStudentData()` (line 704)
- Add more `DBHelper` calls

---

### Common Modifications

#### Change Default Panel
**Line 82:** Change `"Register"` to `"Status"` or other panel name

#### Change Welcome Message Format
**Line 92-94:** Modify the welcome text string

#### Add Confirmation Dialog Before Submit
**In `handleSubmitRegistration()`, before line 558:**
```java
int confirm = JOptionPane.showConfirmDialog(
    studentPanel,
    "Are you sure you want to submit?",
    "Confirm Submission",
    JOptionPane.YES_NO_OPTION
);
if (confirm != JOptionPane.YES_OPTION) {
    return;  // Cancel submission
}
```

#### Change File Types for Material
**Line 470-471:** Modify file filter:
```java
FileNameExtensionFilter filter = new FileNameExtensionFilter(
    "PDF Files Only", "pdf");  // Only PDF
```

#### Add More Presentation Types
**Line 450:** Modify array:
```java
String[] presentationTypes = {"", "Oral", "Poster", "Video", "Hybrid"};
```

---

## Important Notes

1. **Always refresh panels after data changes** - Use refresh methods to update UI
2. **State variables control UI** - Check `registeredSeminar`, `submissionStatus` before showing content
3. **CardLayout names are important** - Must match when calling `cardLayout.show()`
4. **GridBagLayout rows** - Adjust `gbc.gridy` when adding/removing fields
5. **Validation happens before save** - Always validate in `handleSubmitRegistration()` before database call
6. **File paths** - Material path is stored as string (absolute path), not file object

---

## Debugging Tips

1. **Check state variables** - Print `registeredSeminar`, `submissionStatus` to see current state
2. **Check CardLayout names** - Ensure panel names match when switching
3. **Check field references** - Ensure fields are created before use (not null)
4. **Check database calls** - Verify `DBHelper` methods return expected data
5. **Use logger** - Add `logger.info()` statements to track flow

---

## Summary

**StudentPanel** is a complex UI component that:
- Uses CardLayout to switch between screens
- Manages student registration workflow
- Validates form input
- Saves data to database
- Displays student status

**Key methods to understand:**
- `initializePanel()` - Sets up everything
- `handleSubmitRegistration()` - Core registration logic
- `setUser()` - User switching logic
- `createRegistrationFormPanel()` - Form creation

**Key variables:**
- `registeredSeminar` - Confirmed registration
- `selectedSeminarTemp` - Temporary selection
- Form fields - User input

Now you have everything you need to edit and modify StudentPanel! 🎉
