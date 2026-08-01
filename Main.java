import java.util.Scanner;

public class Main {

    static class CarbonRecord {
        String date;
        String transport;
        double distance;
        double electricity;
        double emission;

        CarbonRecord(String date, String transport, double distance,
                     double electricity, double emission) {
            this.date = date;
            this.transport = transport;
            this.distance = distance;
            this.electricity = electricity;
            this.emission = emission;
        }

        void display() {
            System.out.println("-----------------------------------------");
            System.out.println("Date              : " + date);
            System.out.println("Transport         : " + transport);
            System.out.println("Distance (km)     : " + distance);
            System.out.println("Electricity Units : " + electricity);
            System.out.printf("Carbon Emission   : %.2f kg CO2%n", emission);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        CarbonRecord[] records = new CarbonRecord[100];
        int count = 0;

        int choice;

        do {

            System.out.println("\n=======================================");
            System.out.println("      CARBON FOOTPRINT TRACKER");
            System.out.println("=======================================");
            System.out.println("1. Add Carbon Record");
            System.out.println("2. Display All Records");
            System.out.println("3. Exit");
            System.out.print("Enter your choice : ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    if (count == records.length) {
                        System.out.println("Storage Full!");
                        break;
                    }

                    System.out.print("Enter Date (DD/MM/YYYY): ");
                    String date = sc.nextLine();

                    System.out.println("\nSelect Transport");
                    System.out.println("1. Bike");
                    System.out.println("2. Car");
                    System.out.println("3. Bus");
                    System.out.print("Choice: ");

                    int type = sc.nextInt();

                    String transport = "";
                    double factor = 0;

                    switch (type) {

                        case 1:
                            transport = "Bike";
                            factor = 0.10;
                            break;

                        case 2:
                            transport = "Car";
                            factor = 0.20;
                            break;

                        case 3:
                            transport = "Bus";
                            factor = 0.08;
                            break;

                        default:
                            System.out.println("Invalid Transport!");
                            continue;
                    }

                    System.out.print("Enter Distance (km): ");
                    double distance = sc.nextDouble();

                    System.out.print("Enter Electricity Units: ");
                    double electricity = sc.nextDouble();

                    double transportEmission = distance * factor;
                    double electricityEmission = electricity * 0.82;

                    double totalEmission =
                            transportEmission + electricityEmission;

                    records[count] = new CarbonRecord(
                            date,
                            transport,
                            distance,
                            electricity,
                            totalEmission
                    );

                    count++;

                    System.out.println("\nRecord Added Successfully!");

                    break;
                    case 2:

                    if (count == 0) {
                        System.out.println("\nNo Records Found!");
                    } else {

                        System.out.println("\n========== ALL RECORDS ==========");

                        for (int i = 0; i < count; i++) {
                            records[i].display();
                        }
                    }

                    break;

                case 3:

                    System.out.println("\nThank You for Using Carbon Footprint Tracker!");
                    break;

                default:

                    System.out.println("\nInvalid Choice!");

            }

        } while (choice != 3);

        sc.close();

    }

}