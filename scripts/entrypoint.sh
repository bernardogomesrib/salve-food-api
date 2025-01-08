#!/bin/bash
set -e

# Remove all .gitkeep files in /var/lib/postgresql/data
find /var/lib/postgresql/data -name ".gitkeep" -type f -delete

# Change ownership of the data directory
chown -R postgres:postgres /var/lib/postgresql/data

# Set the correct permissions for the data directory
chmod 0750 /var/lib/postgresql/data

# Execute the original command as the postgres user
exec gosu postgres "$@"