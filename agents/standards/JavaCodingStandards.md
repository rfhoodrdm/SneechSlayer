# Coding Standards
- Declare interfaces for service methods.
- Service classes implement interfaces
- Dependencies on services are injected via interface reference.
- Use Slf4j for logging.
- Use variables as temporary labels so that you don't shove everything into one line. For example: 
    - var result = computeResult();
    - processResult(result);
    - This as opposed to processResult(computeResult());
- Services each get their own package.