import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CarShop {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private List<Car> carList = new ArrayList<>();
    final long CARACCEPTTIME = 3000;
    final long WAITINGTIME = 2000;
    final long CARQUANTITY = 5;

    public List<Car> getCarList(){
        return carList;
    }

    public void receiveCar() {
        try {
            lock.lock();
            for (int i = 0; i < CARQUANTITY; i++) {
                System.out.printf("%s принимает новый автомобиль\n", Thread.currentThread().getName());
                Thread.sleep(CARACCEPTTIME);
                System.out.println("Приемка завершена");
                getCarList().add(new Car());
                condition.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void sellCar() {
        try {
            lock.lock();
            System.out.printf("%s заходит в салон\n", Thread.currentThread().getName());
            while (carList.size() == 0) {
                System.out.println("Машин нет в наличии");
                condition.await();
            }
            Thread.sleep(WAITINGTIME);
            System.out.println(Thread.currentThread().getName() + " уехал на новой машине");
            getCarList().remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}