#!/bin/bash
# JHipster Development Container Shell Helper
# Provides easy access to the JHipster development container

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Container name
CONTAINER_NAME="jhipster-dev"

# Function to print colored messages
info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
check_docker() {
    if ! docker info &> /dev/null; then
        error "Docker is not running. Please start Docker and try again."
        exit 1
    fi
}

# Check if container exists
check_container() {
    if ! docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
        error "Container '${CONTAINER_NAME}' does not exist."
        warning "Run 'docker-compose -f docker-compose.jhipster.yml up -d' to create it."
        exit 1
    fi
}

# Check if container is running
check_running() {
    if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
        warning "Container '${CONTAINER_NAME}' is not running."
        info "Starting container..."
        docker-compose -f docker-compose.jhipster.yml up -d jhipster-dev
        sleep 2
    fi
}

# Main function
main() {
    check_docker
    check_container
    check_running

    # If arguments provided, execute command in container
    if [ $# -gt 0 ]; then
        info "Executing in container: $*"
        docker exec -it -u jhipster "${CONTAINER_NAME}" "$@"
    else
        # Interactive shell
        success "Opening interactive shell in '${CONTAINER_NAME}'..."
        info "You are now working as user 'jhipster' in /workspace/evergreen"
        info "Java version: 21 | Node.js version: 22.13.1"
        info "Use 'jhipster' or 'jh' commands to start generating your application"
        echo ""
        docker exec -it -u jhipster "${CONTAINER_NAME}" /bin/bash
    fi
}

# Help text
if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    cat << EOF
JHipster Development Container Shell Helper

USAGE:
    $0                  Open interactive shell in development container
    $0 [command]        Execute command in development container

EXAMPLES:
    $0                                  # Interactive shell
    $0 jhipster                         # Start JHipster generator
    $0 jhipster jdl model.jdl           # Generate entities from JDL file
    $0 ./mvnw                           # Run Maven wrapper
    $0 ./mvnw spring-boot:run           # Start Spring Boot application
    $0 npm start                        # Start frontend development server
    $0 java -version                    # Check Java version
    $0 node --version                   # Check Node version

ENVIRONMENT:
    Container: ${CONTAINER_NAME}
    User: jhipster (non-root)
    Workspace: /workspace/evergreen
    Java: 21 (OpenJDK)
    Node.js: 22.13.1
    JHipster: 8.7.3

GETTING STARTED:
    1. Enter the container: $0
    2. Generate application: jhipster
    3. Create entities: jhipster jdl yourmodel.jdl
    4. Run backend: ./mvnw spring-boot:run
    5. Run frontend: npm start (in separate terminal)

SECURITY:
    - Runs as non-root user (UID/GID 1000)
    - No new privileges allowed
    - Isolated network
    - Resource limits: 8GB RAM, 8 CPUs

For more information, visit: https://www.jhipster.tech/documentation-archive/v8.7.3
EOF
    exit 0
fi

main "$@"

