/*
 * SonarQube
 * Copyright (C) 2009-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v202504;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.sonar.db.MigrationDbTester;
import org.sonar.server.platform.db.migration.step.DdlChange;

import static org.sonar.db.MigrationDbTester.createForMigrationStep;
import static org.sonar.server.platform.db.migration.version.v202504.CreateUniqueIndexOnScaLicenseProfiles.COLUMN_NAME_NAME;
import static org.sonar.server.platform.db.migration.version.v202504.CreateUniqueIndexOnScaLicenseProfiles.COLUMN_NAME_ORG;
import static org.sonar.server.platform.db.migration.version.v202504.CreateUniqueIndexOnScaLicenseProfiles.INDEX_NAME;
import static org.sonar.server.platform.db.migration.version.v202504.CreateUniqueIndexOnScaLicenseProfiles.TABLE_NAME;

class CreateUniqueIndexOnScaLicenseProfilesIT {
  @RegisterExtension
  public final MigrationDbTester db = createForMigrationStep(org.sonar.server.platform.db.migration.version.v202504.CreateUniqueIndexOnScaLicenseProfiles.class);
  private final DdlChange underTest = new CreateUniqueIndexOnScaLicenseProfiles(db.database());

  @Test
  void execute_shouldCreateIndex() throws SQLException {
    db.assertIndexDoesNotExist(TABLE_NAME, INDEX_NAME);
    underTest.execute();
    db.assertUniqueIndex(TABLE_NAME, INDEX_NAME, COLUMN_NAME_ORG, COLUMN_NAME_NAME);
  }

  @Test
  void execute_shouldBeReentrant() throws SQLException {
    db.assertIndexDoesNotExist(TABLE_NAME, INDEX_NAME);
    underTest.execute();
    underTest.execute();
    db.assertUniqueIndex(TABLE_NAME, INDEX_NAME, COLUMN_NAME_ORG, COLUMN_NAME_NAME);
  }
}
