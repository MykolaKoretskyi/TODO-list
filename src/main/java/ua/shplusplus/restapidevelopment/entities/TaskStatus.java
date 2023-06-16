package ua.shplusplus.restapidevelopment.entities;

import java.util.Set;

public enum TaskStatus {

  PLANNED {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {
      return Set.of(
          PLANNED,
          WORK_IN_PROGRESS,
          POSTPONED,
          NOTIFIED,
          SIGNED,
          DONE,
          CANCELLED
      );
    }
  },

  WORK_IN_PROGRESS {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {
      return Set.of(
          WORK_IN_PROGRESS,
          NOTIFIED,
          SIGNED,
          DONE,
          CANCELLED
      );
    }
  },

  POSTPONED {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {

      return Set.of(
          POSTPONED,
          NOTIFIED,
          SIGNED,
          DONE,
          CANCELLED
      );
    }
  },

  NOTIFIED {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {

      return Set.of(
          NOTIFIED,
          DONE,
          CANCELLED
      );
    }
  },

  SIGNED {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {

      return Set.of(
          SIGNED,
          DONE,
          CANCELLED
      );
    }
  },

  DONE {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {
      return Set.of(DONE);
    }
  },

  CANCELLED {
    @Override
    public Set<TaskStatus> getAvailableStatuses() {
      return Set.of(CANCELLED);
    }
  };

  public abstract Set<TaskStatus> getAvailableStatuses();

}
