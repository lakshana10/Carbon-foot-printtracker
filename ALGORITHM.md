# Algorithm — Carbon Footprint Tracker

## 1. Start

**Step 1:** Start the program.
**Step 2:** Load existing data (if any) from `users.csv` and `activities.csv` into memory using `FileManager.loadUsers()` and `FileManager.loadActivities()`.
**Step 3:** Display the main menu in a loop until the user chooses to exit.

---

## 2. Main Menu Algorithm

```
Step 1: Repeat until choice == 0:
   Step 1.1: Display menu options (1–10, 0 to exit)
   Step 1.2: Read user choice
   Step 1.3: Call the corresponding module based on choice
Step 2: On choice == 0:
   Step 2.1: Save all users and activities to file
   Step 2.2: Terminate program
```

---

## 3. User Management Algorithm

### 3.1 Register User
```
Step 1: Read name and email from user
Step 2: Generate a new unique userId (auto-increment counter)
Step 3: Create a User object with (userId, name, email)
Step 4: Insert into users HashMap → key = userId
Step 5: Initialize an empty activity list for this userId
```

### 3.2 View Users
```
Step 1: If users map is empty, display "No users found"
Step 2: Else, iterate over users map and print each User object
```

### 3.3 Update User
```
Step 1: Display user list
Step 2: Read userId to update
Step 3: If userId not found in map, display error and exit step
Step 4: Read new name/email (keep old value if input is blank)
Step 5: Update the User object's fields
```

### 3.4 Delete User
```
Step 1: Display user list
Step 2: Read userId to delete
Step 3: If not found, display error
Step 4: Remove userId entry from users map
Step 5: Remove all activities belonging to that userId from
        activitiesById map and userActivities map (cascade delete)
```

---

## 4. Activity Management Algorithm

### 4.1 Log Activity
```
Step 1: If no users exist, prompt to register a user first
Step 2: Display user list; read userId
Step 3: If userId invalid, display error and exit step
Step 4: Display activity type options: Travel / Electricity / Food / Waste
Step 5: Read selected type
Step 6: Read type-specific inputs:
           Travel      → distance (km), mode (Car/Bike/Bus/Train)
           Electricity → units consumed (kWh)
           Food        → number of meals, diet type (Veg/NonVeg)
           Waste       → quantity of waste (kg)
Step 7: Create the corresponding Activity subclass object
           (auto-generates activityId and today's date)
Step 8: Compute emission using calculateEmission() for that subclass:
           emission = quantity × emission_factor(type, subtype)
Step 9: Insert activity into activitiesById map (key = activityId)
Step 10: Append activity to userActivities[userId] list
Step 11: Display confirmation with computed emission value
```

### 4.2 View All Activities
```
Step 1: If activitiesById is empty, display "No activities logged"
Step 2: Else, iterate and print every Activity object
```

### 4.3 Delete Activity
```
Step 1: Display all activities
Step 2: Read activityId to delete
Step 3: If not found in activitiesById, display error
Step 4: Remove from activitiesById map
Step 5: Remove from the owning user's list in userActivities map
```

---

## 5. Emission Calculation Algorithm (core logic)

```
Function calculateEmission(activity):
   type ← activity.getType()

   If type == "Travel":
        factor ← FACTORS_TRAVEL[mode]      // Car:0.21, Bike:0.0, Bus:0.10, Train:0.04
   Else If type == "Electricity":
        factor ← 0.82                       // kg CO2 per kWh
   Else If type == "Food":
        factor ← FACTORS_FOOD[dietType]     // Veg:0.5, NonVeg:2.5
   Else If type == "Waste":
        factor ← 0.1                        // kg CO2 per kg

   emission ← activity.quantity × factor
   Return emission
```

Each activity subclass overrides this function with its own factor lookup (polymorphism), so the caller never needs to know which subclass it is dealing with.

---

## 6. Emission Summary Algorithm (per user)

```
Step 1: Read userId
Step 2: If userId not in userActivities map, display error
Step 3: total ← 0
Step 4: categoryTotals ← empty map
Step 5: For each activity in userActivities[userId]:
           Step 5.1: Print activity details
           Step 5.2: e ← activity.calculateEmission()
           Step 5.3: total ← total + e
           Step 5.4: categoryTotals[activity.type] ← categoryTotals[activity.type] + e
Step 6: Display total and category-wise breakdown
```

---

## 7. Eco Tips Algorithm

```
Step 1: Read userId (0 for general tips)
Step 2: If userId == 0 OR user has no activities:
           Display generic eco-friendly tips
           Exit
Step 3: For each activity of the user:
           categoryTotals[type] ← categoryTotals[type] + emission
Step 4: topCategory ← category with maximum value in categoryTotals
Step 5: Display a tip specific to topCategory
           (Travel → use public transport, Electricity → save power,
            Food → reduce non-veg meals, Waste → recycle/compost)
```

---

## 8. Reports Algorithm

```
Step 1: totalEmission ← 0, categoryTotals ← empty map
Step 2: For each activity in activitiesById:
           e ← activity.calculateEmission()
           totalEmission ← totalEmission + e
           categoryTotals[activity.type] ← categoryTotals[activity.type] + e
Step 3: Display:
           - Total Users = size of users map
           - Total Activities = size of activitiesById map
           - Total Emission = totalEmission
           - Category-wise emission breakdown
           - Top Emission Category = category with max value in categoryTotals
           - Average Emission per User = totalEmission / total users
```

---

## 9. File Persistence Algorithm

### 9.1 Save
```
Step 1: For each user in users map → write "userId,name,email" to users.csv
Step 2: For each activity in activitiesById → write
        "activityId,userId,type,subType,quantity,date" to activities.csv
```

### 9.2 Load
```
Step 1: If users.csv exists:
           Read line by line → split by comma → reconstruct User object
           → insert into users map
Step 2: If activities.csv exists:
           Read line by line → split by comma
           → call ActivityFactory.fromCsv(fields) to recreate the correct
             subclass (Travel/Electricity/Food/Waste) with original id & date
           → insert into activitiesById map and userActivities map
```

---

## 10. End
```
Step 1: On exit (choice == 0), save all in-memory data to file
Step 2: Terminate the program
```

---

## Complexity Summary

| Operation                     | Data Structure Used        | Time Complexity |
|--------------------------------|-----------------------------|------------------|
| Add / Find / Update / Delete User | HashMap<Integer, User>   | O(1)             |
| Add / Find / Delete Activity   | HashMap<Integer, Activity>  | O(1)             |
| Get all activities of a user   | HashMap<Integer, List<Activity>> | O(1) to fetch list, O(k) to read k activities |
| Emission factor lookup         | HashMap<String, Double>     | O(1)             |
| Category-wise report           | Single pass with HashMap.merge | O(n)          |
| Save / Load from file          | Sequential file I/O         | O(n)             |
