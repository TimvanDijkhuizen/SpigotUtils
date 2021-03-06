package nl.timvandijkhuizen.spigotutils.helpers;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import nl.timvandijkhuizen.spigotutils.SpigotUtils;
import nl.timvandijkhuizen.spigotutils.functions.UnsafeRunnable;
import nl.timvandijkhuizen.spigotutils.functions.UnsafeSupplier;

public class ThreadHelper {

    public static void execute(Runnable task) {
        Bukkit.getScheduler().runTask(SpigotUtils.getInstance(), task);
    }

    public static <T> void executeAsync(UnsafeRunnable task) {
        executeAsync(task, null, null);
    }

    public static <T> void executeAsync(UnsafeRunnable task, Runnable onSuccess) {
        executeAsync(task, onSuccess, null);
    }

    public static <T> void executeAsync(UnsafeRunnable task, Runnable onSuccess, Consumer<Throwable> onError) {
        Bukkit.getScheduler().runTaskAsynchronously(SpigotUtils.getInstance(), () -> {
            try {
                task.run();

                if (onSuccess != null) {
                    execute(onSuccess);
                }
            } catch (Throwable e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        });
    }

    public static <T> void getAsync(UnsafeSupplier<T> task, Consumer<T> onSuccess) {
        getAsync(task, onSuccess, null);
    }

    public static <T> void getAsync(UnsafeSupplier<T> task, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        Bukkit.getScheduler().runTaskAsynchronously(SpigotUtils.getInstance(), () -> {
            try {
                T value = task.get();

                if (onSuccess != null) {
                    execute(() -> onSuccess.accept(value));
                }
            } catch (Throwable e) {
                if (onError != null) {
                    onError.accept(e);
                }
            }
        });
    }

}
