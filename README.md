# Car Crash Java

Command-line driving simulation in Java 8 (works on newer JDKs too). Needs a JDK on the PATH, nothing else.

    ./start.sh    # build and run
    ./test.sh     # run the tests

If the scripts aren't executable after unzipping: `chmod +x start.sh test.sh`.
Everything lives in memory and the build output goes to a temp folder, so each run starts clean.

## How to use it

1. Enter the field size, e.g. `10 10` (valid cells are 0..9 in each direction).
2. `[1]` adds a car: name, then `x y Direction` (e.g. `1 2 N`), then commands (e.g. `FFRFL`).
3. `[2]` runs the simulation and prints where each car ended up.
4. Then start over with a new field, or exit.

Bad input just prints why and asks again. Ctrl-D exits.

## Layout

- `Simulation` – the engine, no I/O, takes a field + cars and returns outcomes
- `SimulationSession` – field and cars, checks names and start positions
- `Cli` / `Main` – console side, input/output injected so it can be tested
- `Car`, `Field`, `Position`, `Direction`, `Command`, `CarOutcome` – small model classes

Tests use a tiny homemade runner instead of JUnit, because the brief bans binaries (no Gradle wrapper jar)
and I didn't want to rely on Maven being installed.

## Decisions where the brief was vague

- North is +y, East is +x, (0,0) bottom-left.
- All cars move together: step n = everyone's n-th command. "Step" in the output is that number.
- A crash = two or more cars in the same cell after a step. This matches the sample (step 7 at (5,4)).
- Crashed cars stop and stay put as obstacles. A car arriving later at that spot crashes too; earlier
  crashes keep their original result.
- Cars that swap places in one step pass through each other, no crash.
- A car with fewer commands just parks at its last cell and can be hit.
- Three or more cars in a cell are all listed: `A, collides with B, C at ...`.
- Two cars can't start on the same cell (rejected when adding), so no "step 0" crashes.
- Rejected with a message: unknown direction/command, start outside the field, duplicate or empty name,
  running with no cars, field size that isn't two positive numbers.
- Accepted: lowercase input, an empty command list (car doesn't move).
- `F` off the edge is ignored and the rest of the commands still run.

## Small deviations

The sample prompts have typos ("heigh", "car A" in car B's prompt); I used the correct text.
Extra error messages were added for validation.

AI tools were used while building this, which the brief allows.

## Sample runs

Typed input is shown after each prompt so you can follow along; it's what you'd type and press Enter on.
These are the actual outputs from running `./start.sh`.

### 1. One car, no collision

```
Welcome to Car Crash Java!

Please enter the width and height of the simulation field in x y format:
10 10

You have created a field of 10 x 10.

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
1

Please enter the name of the car:
A

Please enter initial position of car A in x y Direction format:
1 2 N

Please enter the commands for car A:
FFRFFFFRRL

Your current list of cars are:
- A, (1,2) N, FFRFFFFRRL

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
2

Your current list of cars are:
- A, (1,2) N, FFRFFFFRRL

After simulation, the result is:
- A, (5,4) S

Please choose from the following options:
[1] Start over
[2] Exit
2

Thank you for running the simulation. Goodbye!
```

### 2. Two cars that collide

```
Welcome to Car Crash Java!

Please enter the width and height of the simulation field in x y format:
10 10

You have created a field of 10 x 10.

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
1

Please enter the name of the car:
A

Please enter initial position of car A in x y Direction format:
1 2 N

Please enter the commands for car A:
FFRFFFFRRL

Your current list of cars are:
- A, (1,2) N, FFRFFFFRRL

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
1

Please enter the name of the car:
B

Please enter initial position of car B in x y Direction format:
7 8 W

Please enter the commands for car B:
FFLFFFFFFF

Your current list of cars are:
- A, (1,2) N, FFRFFFFRRL
- B, (7,8) W, FFLFFFFFFF

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
2

Your current list of cars are:
- A, (1,2) N, FFRFFFFRRL
- B, (7,8) W, FFLFFFFFFF

After simulation, the result is:
- A, collides with B at (5,4) at step 7
- B, collides with A at (5,4) at step 7

Please choose from the following options:
[1] Start over
[2] Exit
2

Thank you for running the simulation. Goodbye!
```

### 3. Bad input, then start over

Shows the error/re-prompt behavior (wrong field format, zero size, off-field position, unknown
direction, invalid command) and using "Start over" to run a second, independent simulation.

```
Welcome to Car Crash Java!

Please enter the width and height of the simulation field in x y format:
abc
Expected two numbers, e.g. '10 10'. Please try again:
0 5
Field width and height must both be positive. Please try again:
5 5

You have created a field of 5 x 5.

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
1

Please enter the name of the car:
A

Please enter initial position of car A in x y Direction format:
9 9 N
Position (9,9) is outside the 5 x 5 field. Please try again:
1 1 Q
Unknown direction 'Q'. Use one of N, E, S, W. Please try again:
1 1 N

Please enter the commands for car A:
FFX
Invalid command 'X'. Only L, R and F are allowed. Please try again:
F

Your current list of cars are:
- A, (1,1) N, F

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
2

Your current list of cars are:
- A, (1,1) N, F

After simulation, the result is:
- A, (1,2) N

Please choose from the following options:
[1] Start over
[2] Exit
1

Please enter the width and height of the simulation field in x y format:
4 4

You have created a field of 4 x 4.

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
1

Please enter the name of the car:
B

Please enter initial position of car B in x y Direction format:
0 0 E

Please enter the commands for car B:
FFFFFF

Your current list of cars are:
- B, (0,0) E, FFFFFF

Please choose from the following options:
[1] Add a car to field
[2] Run simulation
2

Your current list of cars are:
- B, (0,0) E, FFFFFF

After simulation, the result is:
- B, (3,0) E

Please choose from the following options:
[1] Start over
[2] Exit
2

Thank you for running the simulation. Goodbye!
```

Note `B` ends at `(3,0)` on a `4 x 4` field even though it was told to move `F` six times — the last
two `F`s are ignored because `x=3` is already the field's east edge (valid x is `0..3`).