package com.avaje.ebeaninternal.server.type;

/**
 * Detects when content has been modified and as such needs to be persisted (included in an update).
 */
public class ModifyAwareFlag implements ModifyAwareOwner {

  boolean dirty;

  @Override
  public boolean isMarkedDirty() {
    if (!dirty) return false;
    dirty = false;
    return true;
  }

  @Override
  public void markAsModified() {
    dirty = true;
  }

  @Override
  public void resetMarkedDirty() {
    dirty = false;
  }
}
